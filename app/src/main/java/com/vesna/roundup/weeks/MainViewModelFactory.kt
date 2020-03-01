package com.vesna.roundup.weeks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vesna.roundup.domain.GetAllWeeksSinceAccountCreation

class MainViewModelFactory(
    private val getWeeks: GetAllWeeksSinceAccountCreation
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(getWeeks) as T
    }
}