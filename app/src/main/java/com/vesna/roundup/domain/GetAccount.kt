package com.vesna.roundup.domain

import io.reactivex.Single

class GetAccount(private val repo: AccountRepo) {

    fun execute(): Single<Account> {
        return repo.fetchAccount()
            .andThen(repo.getAccount())
            .toSingle()
    }
}