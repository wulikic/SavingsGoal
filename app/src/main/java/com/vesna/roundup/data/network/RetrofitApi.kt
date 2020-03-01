package com.vesna.roundup.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface RetrofitApi {

    @Headers(
        value = [
            "Accept: application/json",
            "User-Agent: Test"
        ]
    )
    @GET("/api/v2/accounts")
    fun accounts(@Header("Authorization") token: String): Single<AccountsResponse>
}