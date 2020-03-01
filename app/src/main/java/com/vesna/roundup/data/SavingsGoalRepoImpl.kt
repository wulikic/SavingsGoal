package com.vesna.roundup.data

import com.vesna.roundup.data.localstorage.SavingsGoalStorage
import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.model.SavingsGoal
import com.vesna.roundup.domain.repo.SavingsGoalRepo
import io.reactivex.Completable
import io.reactivex.Maybe

class SavingsGoalRepoImpl(
    private val api: Api,
    private val localStorage: SavingsGoalStorage)
    : SavingsGoalRepo {

    override fun createSavingsGoal(): Completable {
        return api.createSavingGoal()
            .flatMapCompletable { localStorage.save(it) }
    }

    override fun getSavingsGoal(): Maybe<SavingsGoal> {
        return localStorage.read()
    }
}