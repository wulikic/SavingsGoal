package com.vesna.roundup.data

import com.vesna.roundup.data.localstorage.AccountLocalStorage
import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.Account
import com.vesna.roundup.domain.AccountRepo
import io.reactivex.Completable
import io.reactivex.Maybe

class AccountRepoImpl(
    private val api: Api,
    private val localStorage: AccountLocalStorage
) : AccountRepo {

    override fun fetchAccount(): Completable {
        return api.accounts()
            .flatMapCompletable { list -> localStorage.save(list[0]) }
    }

    override fun getAccount(): Maybe<Account> {
        return localStorage.read()
    }
}