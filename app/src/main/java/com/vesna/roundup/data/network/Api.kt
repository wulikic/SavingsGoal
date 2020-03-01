package com.vesna.roundup.data.network

import com.vesna.roundup.data.localstorage.AccountLocalStorage
import com.vesna.roundup.data.network.model.AddToSavingsGoalRequest
import com.vesna.roundup.data.network.model.CreateSavingsGoalRequest
import com.vesna.roundup.data.network.model.TransactionsResponse
import com.vesna.roundup.domain.errors.ApiError
import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.model.SavingsGoal
import com.vesna.roundup.domain.model.Transaction
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import java.util.*

class Api(
    private val retrofitApi: RetrofitApi,
    token: String,
    private val accountLocalStorage: AccountLocalStorage,
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

    fun createSavingGoal(name: String, currency: String): Single<SavingsGoal> {
        return retrofitApi.createSavingsGoal(
            auth = authHeader,
            accountId = accountLocalStorage.read()!!.accountId,
            body = CreateSavingsGoalRequest.Body(name, currency)
        )
            .map { response ->
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

    fun addMoney(
        savingsGoalId: String,
        accountId: String,
        transferUid: UUID,
        amount: Int,
        currency: String
    ): Completable {
        return retrofitApi.addMoneyToSavingsGoal(
            auth = authHeader,
            accountId = accountId,
            savingsGoalUid = savingsGoalId,
            transferUid = transferUid.toString(),
            body = AddToSavingsGoalRequest.Body(
                AddToSavingsGoalRequest.Amount(
                    currency = currency,
                    minorUnits = amount
                )
            )
        )
            .ignoreElement()
    }

    private fun TransactionsResponse.FeedItem.direction(): Transaction.Direction {
        return when {
            direction.equals("OUT", ignoreCase = true) -> Transaction.Direction.OUT
            direction.equals("IN", ignoreCase = true) -> Transaction.Direction.IN
            else -> throw IllegalArgumentException()
        }
    }
}