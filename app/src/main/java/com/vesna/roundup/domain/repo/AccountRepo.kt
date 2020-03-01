package com.vesna.roundup.domain.repo

import com.vesna.roundup.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Maybe

interface AccountRepo {

    fun fetchAccount(): Completable
    fun getAccount(): Maybe<Account>
}