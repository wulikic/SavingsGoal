package com.vesna.roundup.domain

import io.reactivex.Completable
import io.reactivex.Maybe

interface AccountRepo {

    fun fetchAccount(): Completable
    fun getAccount(): Maybe<Account>
}