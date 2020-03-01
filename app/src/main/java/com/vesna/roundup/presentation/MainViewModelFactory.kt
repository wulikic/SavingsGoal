package com.vesna.roundup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vesna.roundup.domain.usecase.AddToSavingsGoal
import com.vesna.roundup.domain.usecase.CalculateRoundUp
import com.vesna.roundup.domain.usecase.GetAllWeeksSinceAccountCreation
import com.vesna.roundup.presentation.roundup.RoundUpViewModel
import com.vesna.roundup.presentation.weekselection.WeekSelectionViewModel

class MainViewModelFactory(
    private val getWeeks: GetAllWeeksSinceAccountCreation,
    private val calculateRoundUp: CalculateRoundUp,
    private val addToSavingsGoal: AddToSavingsGoal
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            WeekSelectionViewModel::class.java -> WeekSelectionViewModel(getWeeks) as T
            RoundUpViewModel::class.java -> RoundUpViewModel(calculateRoundUp, addToSavingsGoal) as T
            else -> throw IllegalArgumentException()
        }
    }
}