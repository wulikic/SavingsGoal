package com.vesna.roundup.data.network.model

interface AddToSavingsGoalRequest {

    data class Body(val amount: Amount)

    data class Amount(val currency: String, val minorUnits: Int)
}
