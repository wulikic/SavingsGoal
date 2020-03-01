package com.vesna.roundup.domain.usecase

import com.vesna.roundup.domain.model.SavingsGoal
import com.vesna.roundup.domain.repo.SavingsGoalRepo
import io.reactivex.Single

class GetOrCreateSavingsGoal(private val repo: SavingsGoalRepo) {

    fun execute(): Single<SavingsGoal> {
        return repo.getSavingsGoal()
            .switchIfEmpty(
                repo.createSavingsGoal()
                    .andThen(repo.getSavingsGoal().toSingle())
            ) // TODO add errors
    }
}