package com.vesna.roundup.domain.model

data class ApiError(val errorMessage: String? = null) : Exception(errorMessage)