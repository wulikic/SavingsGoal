package com.vesna.roundup.domain.repo

import com.vesna.roundup.domain.model.SavingsGoal
import io.reactivex.Completable
import io.reactivex.Maybe

interface SavingsGoalRepo {

    fun createSavingsGoal(): Completable
    fun getSavingsGoal(): Maybe<SavingsGoal>
}