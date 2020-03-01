package com.vesna.roundup.data.network.model

interface AccountsResponse {

    data class Accounts(
        val accounts: List<Account>
    )

    data class Account(
        val accountUid: String,
        val defaultCategory: String,
        val currency: String,
        val createdAt: String
    )
}