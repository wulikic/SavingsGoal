package com.vesna.roundup.domain.usecase

import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.model.SavingsGoal
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*

class AddToSavingsGoal(
    private val api: Api,
    private val getAccount: GetAccount,
    private val getOrCreateSavingsGoal: GetOrCreateSavingsGoal
) {

    fun execute(amount: Int): Completable {
        return Single.zip(
            getAccount.execute(),
            getOrCreateSavingsGoal.execute(),
            BiFunction<Account, SavingsGoal, Pair<Account, SavingsGoal>> { account, savingsGoal ->
                Pair(
                    account,
                    savingsGoal
                )
            })
            .flatMapCompletable { pair ->
                api.addMoney(
                    savingsGoalId = pair.second.uid,
                    accountId = pair.first.accountId,
                    transferUid = UUID.randomUUID(),
                    amount = amount,
                    currency = "GBP"
                )
            }
    }
}