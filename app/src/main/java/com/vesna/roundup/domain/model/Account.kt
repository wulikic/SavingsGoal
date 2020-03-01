package com.vesna.roundup.domain.model

import org.joda.time.DateTime

data class Account(
    val accountId: String,
    val defaultCategory: String,
    val createdAt: DateTime
)