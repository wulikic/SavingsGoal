package com.vesna.roundup.domain

import io.reactivex.Single
import org.joda.time.DateTime

class GetWeeks {

    fun execute(from: DateTime, downTo: DateTime, firstWeekDay: WeekDay): Single<List<Period>> {
        return Single.fromCallable {
            val list = mutableListOf<Period>()
            val currentWeekStart = from.withDayOfWeek(firstWeekDay.value)
            val oldestWeekEnd = downTo.withDayOfWeek(firstWeekDay.value).plusDays(6)

            list.add(Period(currentWeekStart, from))

            var day = currentWeekStart.minusWeeks(1)
            while (day.isAfter(downTo)) { // TODO check this
                list.add(Period(day, day.plusDays(6)))
                day = day.minusWeeks(1)
            }

            list.add(Period(downTo, oldestWeekEnd))

            list
        }
//        return getAccount.execute().map { account ->
//            val oldest = account.createdAt
//            val latest = Date()
//            val calendar = Calendar.getInstance()
//
//            val list = mutableListOf<Period>()
//
//            var cursor = latest
//            while (cursor.before(oldest)) { // TODO check this
//                calendar.time = cursor
//                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
//                val weekStart = calendar.time
//                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek + 6)
//                val weekEnd = calendar.time
//                list.add(Period(weekStart, weekEnd))
//                cursor = Date(cursor.time - 1)
//            }
//
//            listOf(Period(oldest, latest))
//        }
    }
}