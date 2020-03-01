package com.vesna.roundup.data.network

import com.vesna.roundup.data.network.model.*
import io.reactivex.Single
import retrofit2.http.*

interface RetrofitApi {

    @Headers(value = ["Accept: application/json", "User-Agent: Test"])
    @GET("/api/v2/accounts")
    fun accounts(@Header("Authorization") auth: String): Single<AccountsResponse.Accounts>


    @Headers(value = ["Accept: application/json", "User-Agent: Test"])
    @PUT("/api/v2/account/{accountUid}/savings-goals")
    fun createSavingsGoal(@Header("Authorization") auth: String): Single<CreateSavingsGoalResponse.SavingsGoal>

    @Headers(value = ["Accept: application/json", "User-Agent: Test"])
    @GET("/api/v2/feed/account/{accountUid}/category/{categoryUid}/transactions-between")
    fun transactions(
        @Header("Authorization") auth: String,
        @Path("accountUid") accountId: String,
        @Path("categoryUid") categoryUid: String,
        @Query("minTransactionTimestamp") from: String,
        @Query("maxTransactionTimestamp") to: String): Single<TransactionsResponse.FeedItems>

    @Headers(value = ["Accept: application/json", "User-Agent: Test"])
    @GET("/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    fun addMoneyToSavingsGoal(
        @Header("Authorization") auth: String,
        @Path("accountUid") accountId: String,
        @Path("savingsGoalUid") savingsGoalUid: String,
        @Query("transferUid") transferUid: String,
        @Body body: AmountBodyRequest
    ): Single<AddMoneyToSavingsGoalResponse.SavingsGoalTransfer>

}