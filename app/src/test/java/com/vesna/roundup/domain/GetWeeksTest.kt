package com.vesna.roundup.domain

import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.model.WeekDay
import com.vesna.roundup.domain.usecase.GetWeeks
import io.mockk.MockKAnnotations
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Before
import org.junit.Test

class GetWeeksTest {

    lateinit var sut: GetWeeks

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        sut = GetWeeks()
    }

    @Test
    fun multipleWeeks() {
        val from = DateTime(2020, 2, 26, 0, 0, DateTimeZone.UTC)
        val downTo = DateTime(2020, 2, 9, 0, 0, DateTimeZone.UTC)

        val expected = listOf(
            Period(
                DateTime(2020, 2, 24, 0, 0, DateTimeZone.UTC),
                DateTime(2020, 3, 1, 23, 59, 59, 999, DateTimeZone.UTC)
            ),
            Period(
                DateTime(2020, 2, 17, 0, 0, DateTimeZone.UTC),
                DateTime(2020, 2, 23, 23, 59, 59, 999, DateTimeZone.UTC)
            ),
            Period(
                DateTime(2020, 2, 10, 0, 0, DateTimeZone.UTC),
                DateTime(2020, 2, 16, 23, 59, 59, 999, DateTimeZone.UTC)
            ),
            Period(
                DateTime(2020, 2, 3, 0, 0, DateTimeZone.UTC),
                DateTime(2020, 2, 9, 23, 59, 59, 999, DateTimeZone.UTC)
            )
        )

        sut.execute(from, downTo, WeekDay.MONDAY).test().assertValue(expected)
    }

    @Test
    fun oneWeek() {
        val from = DateTime(2020, 3, 1, 0, 0, DateTimeZone.UTC)
        val downTo = DateTime(2020, 2, 27, 0, 0, DateTimeZone.UTC)

        val expected = listOf(
            Period(
                DateTime(2020, 2, 24, 0, 0, DateTimeZone.UTC),
                DateTime(2020, 3, 1, 23, 59, 59, 999, DateTimeZone.UTC)
            )
        )

        sut.execute(from, downTo, WeekDay.MONDAY).test().assertValue(expected)
    }
}