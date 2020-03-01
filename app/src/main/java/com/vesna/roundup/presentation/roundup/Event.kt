package com.vesna.roundup.presentation.roundup

sealed class Event
object MoneyTransferSucceeded : Event()
object MoneyTransferFailed : Event()
object FindingRoundUpFailed : Event()
