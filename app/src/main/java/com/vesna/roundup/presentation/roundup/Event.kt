package com.vesna.roundup.presentation.roundup

sealed class Event
object Success : Event()
object Failure : Event()