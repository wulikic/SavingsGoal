package com.vesna.roundup.weeks

import com.vesna.roundup.domain.Period
import org.joda.time.format.DateTimeFormatter

data class UiPeriod(val period: Period, val formatter: DateTimeFormatter) {
    override fun toString(): String {
        return String.format("%s to %s", period.from.toString(formatter), period.to.toString(formatter))
    }
}