package com.vesna.roundup.presentation.weekselection

sealed class WeekSelectionEvent
object ProblemFetchingAccount : WeekSelectionEvent()
object GenericError : WeekSelectionEvent()