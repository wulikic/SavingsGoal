package com.vesna.roundup.domain

import io.reactivex.Single
import org.joda.time.DateTime

class GetAllWeeksSinceAccountCreation(
    private val getAccount: GetAccount,
    private val getWeeks: GetWeeks
) {

    fun execute(): Single<List<Period>> {
        return getAccount.execute().flatMap { account ->
            getWeeks.execute(
                from = DateTime.now(),
                downTo = account.createdAt,
                firstWeekDay = WeekDay.MONDAY
            )
        }
    }
}