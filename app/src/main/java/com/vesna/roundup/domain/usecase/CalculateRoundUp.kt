package com.vesna.roundup.domain.usecase

import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.model.Transaction
import io.reactivex.Single
import kotlin.math.ceil

// only for out transactions
class CalculateRoundUp(private val api: Api, private val getAccount: GetAccount) {

    fun execute(period: Period): Single<Double> {
        return getAccount.execute().flatMap { account ->
            api.getTransactions(period.from, period.to, account)
                .map { list ->
                    list.filter { it.direction == Transaction.Direction.OUT }
                        .map { transaction ->
                            ceil(transaction.amount) - transaction.amount
                        }.sum()
                }
        }
    }
}