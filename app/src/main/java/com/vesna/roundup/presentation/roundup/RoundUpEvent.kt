package com.vesna.roundup.presentation.roundup

sealed class RoundUpEvent
object MoneyTransferSucceeded : RoundUpEvent()
object MoneyTransferFailed : RoundUpEvent()
object FindingRoundUpFailed : RoundUpEvent()
