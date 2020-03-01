package com.vesna.roundup.data.localstorage

import android.content.SharedPreferences
import com.vesna.roundup.domain.model.SavingsGoal
import io.reactivex.Completable
import io.reactivex.Maybe

class SavingsGoalStorage(private val sharedPref: SharedPreferences) {

    fun save(savingsGoal: SavingsGoal): Completable {
        return Completable.fromAction {
            sharedPref.edit().apply {
                putString("uid", savingsGoal.uid)
            }.apply()
        }
    }

    fun read(): Maybe<SavingsGoal> {
        return Maybe.fromCallable {
            val uid = sharedPref.getString("uid", null)
                ?: return@fromCallable null
            SavingsGoal(uid = uid)
        }
    }
}