package com.vesna.roundup.domain.usecase

import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.model.WeekDay
import io.reactivex.Single
import org.joda.time.DateTime

class GetAllWeeksSinceAccountCreation(
    private val getAccount: GetAccount,
    private val getWeeks: GetWeeks
) {

    fun execute(until: DateTime): Single<List<Period>> {
        return getAccount.execute().flatMap { account ->
            getWeeks.execute(
                from = until,
                downTo = account.createdAt,
                firstWeekDay = WeekDay.MONDAY
            )
        }
    }
}