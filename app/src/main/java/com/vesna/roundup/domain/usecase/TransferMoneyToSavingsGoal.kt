package com.vesna.roundup.domain.usecase

import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.errors.TransferingMoneyToSavingsGoalFailed
import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.model.SavingsGoal
import io.reactivex.Completable
import java.util.*

class TransferMoneyToSavingsGoal(private val api: Api) {

    fun execute(savingsGoal: SavingsGoal, account: Account, amount: Int, currency: String, transferUUID: UUID): Completable {
        return api.addMoney(
            savingsGoalId = savingsGoal.uid,
            accountId = account.accountId,
            transferUid = transferUUID,
            amount = amount,
            currency = currency
        ).onErrorResumeNext { Completable.error(TransferingMoneyToSavingsGoalFailed) }
    }
}