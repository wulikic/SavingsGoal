package com.vesna.roundup.data.network.model

interface AddMoneyToSavingsGoalResponse {

    data class SavingsGoalTransfer(val transferUid: String, val success: Boolean)
}