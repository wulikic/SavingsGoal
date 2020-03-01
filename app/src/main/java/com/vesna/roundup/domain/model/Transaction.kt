package com.vesna.roundup.domain.model

data class Transaction(val amountInP: Int, val direction: Direction) {

    enum class Direction {
        IN,
        OUT
    }
}