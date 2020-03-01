package com.vesna.roundup.domain.usecase

import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.model.WeekDay
import io.reactivex.Single
import org.joda.time.DateTime

class GetWeeks {

    fun execute(from: DateTime, downTo: DateTime, firstWeekDay: WeekDay): Single<List<Period>> {
        return Single.fromCallable {
            val list = mutableListOf<Period>()
            val currentWeekStart = from.withDayOfWeek(firstWeekDay.value).withTime(0, 0, 0, 0)
            val oldestWeekStart = downTo.withDayOfWeek(firstWeekDay.value).withTime(0, 0, 0, 0)

            var day = currentWeekStart
            while (day.isAfter(oldestWeekStart) || day == oldestWeekStart) {
                list.add(
                    Period(
                        day,
                        day.plusDays(7).minusMillis(1)
                    )
                )
                day = day.minusWeeks(1)
            }
            list
        }
    }
}