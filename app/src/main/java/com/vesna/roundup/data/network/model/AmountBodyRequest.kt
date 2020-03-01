package com.vesna.roundup.data.network.model

data class AmountBodyRequest(
    val currency: String,
    val minorUnits: Int
)