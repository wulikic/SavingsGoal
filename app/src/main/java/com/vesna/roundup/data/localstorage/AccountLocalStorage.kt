package com.vesna.roundup.data.localstorage

import android.content.SharedPreferences
import com.vesna.roundup.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Maybe
import org.joda.time.DateTime

class AccountLocalStorage(private val sharedPref: SharedPreferences) {

    fun save(account: Account): Completable {
        return Completable.fromAction {
            sharedPref.edit().apply {
                putString("accountId", account.accountId)
                putString("defaultCategory", account.defaultCategory)
                putLong("createdAt", account.createdAt.millis)
            }.apply()
        }
    }

    fun read(): Maybe<Account> {
        return Maybe.fromCallable {
            val accountId = sharedPref.getString("accountId", null)
                ?: return@fromCallable null
            val defaultCategory = sharedPref.getString("defaultCategory", null)
                ?: return@fromCallable null
            val createdAtTimestamp = sharedPref.getLong("createdAt", -1)
            val createdAt =
                if (createdAtTimestamp > 0) DateTime(createdAtTimestamp) else return@fromCallable null

            Account(
                accountId = accountId,
                defaultCategory = defaultCategory,
                createdAt = createdAt
            )
        }
    }
}