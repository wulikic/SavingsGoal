package com.vesna.roundup.data.localstorage

import android.content.SharedPreferences
import com.vesna.roundup.domain.model.SavingsGoal

class SavingsGoalStorage(private val sharedPref: SharedPreferences) {

    fun save(savingsGoal: SavingsGoal) {
        sharedPref.edit().apply {
            putString("uid", savingsGoal.uid)
        }.apply()
    }

    fun read(): SavingsGoal? {
        val uid = sharedPref.getString("uid", null)
            ?: return null
        return SavingsGoal(uid = uid)
    }
}