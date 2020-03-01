package com.vesna.roundup.domain.errors

data class ApiError(val errorMessage: String? = null) : Exception(errorMessage)