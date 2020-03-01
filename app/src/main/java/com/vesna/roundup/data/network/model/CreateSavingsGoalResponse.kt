package com.vesna.roundup.data.network.model

interface CreateSavingsGoalResponse {

    data class SavingsGoal(
        val savingsGoalUid: String,
        val success: Boolean,
        val errors: List<String>
    )
}