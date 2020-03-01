package com.vesna.roundup.data.localstorage

import android.content.SharedPreferences
import com.vesna.roundup.domain.model.Account
import org.joda.time.DateTime

class AccountLocalStorage(private val sharedPref: SharedPreferences) {

    fun save(account: Account) {
        sharedPref.edit().apply {
            putString("accountId", account.accountId)
            putString("defaultCategory", account.defaultCategory)
            putLong("createdAt", account.createdAt.millis)
        }.apply()
    }

    fun read(): Account? {
        val accountId = sharedPref.getString("accountId", null)
            ?: return null
        val defaultCategory = sharedPref.getString("defaultCategory", null)
            ?: return null
        val createdAtTimestamp = sharedPref.getLong("createdAt", -1)
        val createdAt =
            if (createdAtTimestamp > 0) DateTime(createdAtTimestamp) else return null

        return Account(
            accountId = accountId,
            defaultCategory = defaultCategory,
            createdAt = createdAt
        )
    }
}