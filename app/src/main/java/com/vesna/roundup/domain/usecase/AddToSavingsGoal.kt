package com.vesna.roundup.domain.usecase

import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.model.SavingsGoal
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*

class AddToSavingsGoal(
    private val transferMoneyToSavingsGoal: TransferMoneyToSavingsGoal,
    private val getAccount: GetAccount,
    private val getOrCreateSavingsGoal: GetOrCreateSavingsGoal
) {

    fun execute(amount: Int, transferUid: UUID): Completable {
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
                transferMoneyToSavingsGoal.execute(
                    savingsGoal = pair.second,
                    account = pair.first,
                    amount = amount,
                    currency = "GBP",
                    transferUUID = transferUid
                )
            }
    }
}