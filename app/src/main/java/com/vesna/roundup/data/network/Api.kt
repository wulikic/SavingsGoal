package com.vesna.roundup.data.network

import com.vesna.roundup.domain.Account
import io.reactivex.Single
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class Api(private val retrofitApi: RetrofitApi,
          private val token: String) {

    // 2017-05-08T12:34:21.000Z
    private val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    fun accounts(): Single<List<Account>> {
        return retrofitApi.accounts("Bearer $token").map { response ->
            response.accounts.map {
                Account(
                    accountId = it.accountUid,
                    defaultCategory = it.defaultCategory,
                    createdAt = requireNotNull(
                        value = dateFormatter.parseDateTime(it.createdAt),
                        lazyMessage = { "Unexpected date format" }
                    )
                )
            }
        }
    }
}