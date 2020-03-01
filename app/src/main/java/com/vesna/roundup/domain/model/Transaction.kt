package com.vesna.roundup.domain.model

data class Transaction(val amount: Double, val direction: Direction) {

    enum class Direction {
        IN,
        OUT
    }
}