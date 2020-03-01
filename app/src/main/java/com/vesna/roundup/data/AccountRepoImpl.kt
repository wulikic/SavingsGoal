package com.vesna.roundup.data

import com.vesna.roundup.data.localstorage.AccountLocalStorage
import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.errors.FetchingAccountFailed
import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.repo.AccountRepo
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class AccountRepoImpl(
    private val api: Api,
    private val localStorage: AccountLocalStorage
) : AccountRepo {

    override fun fetchAccount(): Completable {
        return api.accounts()
            .onErrorResumeNext { Single.error(FetchingAccountFailed) }
            .flatMapCompletable { list -> Completable.fromAction { localStorage.save(list[0]) } }
    }

    override fun getAccount(): Maybe<Account> {
        return Maybe.fromCallable { localStorage.read() }
    }
}