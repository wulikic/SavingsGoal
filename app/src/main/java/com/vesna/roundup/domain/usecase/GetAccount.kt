package com.vesna.roundup.domain.usecase

import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.repo.AccountRepo
import io.reactivex.Single

class GetAccount(private val repo: AccountRepo) {

    fun execute(): Single<Account> {
        return repo.fetchAccount()
            .andThen(repo.getAccount())
            .toSingle()
    }
}