package com.vesna.roundup.domain.usecase

import com.vesna.roundup.domain.errors.CreatingSavingsGoalFailed
import com.vesna.roundup.domain.model.SavingsGoal
import com.vesna.roundup.domain.repo.SavingsGoalRepo
import io.reactivex.Completable
import io.reactivex.Single

class GetOrCreateSavingsGoal(private val repo: SavingsGoalRepo) {

    fun execute(): Single<SavingsGoal> {
        return repo.getSavingsGoal()
            .switchIfEmpty(
                repo.createSavingsGoal()
                    .onErrorResumeNext { Completable.error(CreatingSavingsGoalFailed) }
                    .andThen(repo.getSavingsGoal().toSingle())
            )
    }
}