package com.vesna.roundup.data.network

data class AccountResponse(
    val accountUid: String,
    val defaultCategory: String,
    val currency: String,
    val createdAt: String
)