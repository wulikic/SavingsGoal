package com.vesna.roundup.data.network

import com.vesna.roundup.data.network.model.TransactionsResponse
import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.model.ApiError
import com.vesna.roundup.domain.model.SavingsGoal
import com.vesna.roundup.domain.model.Transaction
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

class Api(
    private val retrofitApi: RetrofitApi,
    token: String,
    private val apiDateFormatter: DateTimeFormatter
) {

    private val authHeader = "Bearer $token"

    fun accounts(): Single<List<Account>> {
        return retrofitApi.accounts(authHeader).map { response ->
            response.accounts.map {
                Account(
                    accountId = it.accountUid,
                    defaultCategory = it.defaultCategory,
                    createdAt = requireNotNull(value = apiDateFormatter.parseDateTime(it.createdAt))
                )
            }
        }
    }

    fun createSavingGoal(): Single<SavingsGoal> {
        return retrofitApi.createSavingsGoal(authHeader).map { response ->
            if (response.success) {
                SavingsGoal(response.savingsGoalUid)
            } else {
                throw ApiError("Creating savings goal failed")
            }
        }
    }

    fun getTransactions(from: DateTime, to: DateTime, account: Account): Single<List<Transaction>> {
        return retrofitApi.transactions(
            auth = authHeader,
            from = from.toString(apiDateFormatter),
            to = to.toString(apiDateFormatter),
            accountId = account.accountId,
            categoryUid = account.defaultCategory
        )
            .map { response ->
                response.feedItems.map {
                    Transaction(
                        amountInP = it.amount.minorUnits,
                        direction = it.direction()
                    )
                }
            }
    }

    private fun TransactionsResponse.FeedItem.direction(): Transaction.Direction {
        return when {
            direction.equals("OUT", ignoreCase = true) -> Transaction.Direction.OUT
            direction.equals("IN", ignoreCase = true) -> Transaction.Direction.IN
            else -> throw IllegalArgumentException()
        }
    }
}