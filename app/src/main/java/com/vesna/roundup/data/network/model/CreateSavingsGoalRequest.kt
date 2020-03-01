package com.vesna.roundup.data.network.model

interface CreateSavingsGoalRequest {

    data class Body(val name: String,
                    val currency: String)
}