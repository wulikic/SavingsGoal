package com.vesna.roundup.domain.model

import org.joda.time.DateTimeConstants

enum class WeekDay(val value: Int) {
    MONDAY(DateTimeConstants.MONDAY),
    TUESDAY(DateTimeConstants.TUESDAY),
    WEDNESDAY(DateTimeConstants.WEDNESDAY),
    THURSDAY(DateTimeConstants.THURSDAY),
    FRIDAY(DateTimeConstants.FRIDAY),
    SATURDAY(DateTimeConstants.SATURDAY),
    SUNDAY(DateTimeConstants.SUNDAY)
}
