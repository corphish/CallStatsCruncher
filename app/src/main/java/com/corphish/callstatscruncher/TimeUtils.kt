package com.corphish.callstatscruncher

import java.util.concurrent.TimeUnit

object TimeUtils {
    fun toDescription(s: Long): String {
        val sec = s % 60
        val min = TimeUnit.SECONDS.toMinutes(s) % 60
        val hr = TimeUnit.SECONDS.toHours(s)

        return "$hr hr(s) $min min(s) $sec secs"
    }
}