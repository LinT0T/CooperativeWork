package com.greenhand.cooperativework.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
object TimeUtil {

    /**
     * 统一日期格式，如：2021-5-5、2021-5-25
     */
    private val sdf = SimpleDateFormat("yyyy-M-d", Locale.CHINA)

    /**
     * 获取当天日期
     */
    fun getNowDate(): String {
        return sdf.format(Date())
    }

    /**
     * 根据毫秒获取日期
     */
    fun getTime(date: Date): String {
        return sdf.format(date)
    }

    /**
     * 根据秒数获取 xx:xx
     */
    fun getTimeBySecond(duration: Int): java.lang.StringBuilder {
        val time: StringBuilder = java.lang.StringBuilder()
        if (duration / 60 < 10) {
            time.append("0" + duration / 60 + ":")
        } else {
            time.append((duration / 60).toString() + ":")
        }
        if (duration - 60 * (duration / 60) < 10) {
            time.append("0" + (duration - 60 * (duration / 60)))
        } else {
            time.append(duration - 60 * (duration / 60))
        }
        return time
    }

    /**
     * 获取两个日期之间的间隔天数
     * @return 两个日期之间的间隔天数
     */
    fun getDiffDate(start: String, end: String): Int {
        val startDate = sdf.parse(start)
        val endDate = sdf.parse(end)
        val fromCalendar = Calendar.getInstance()
        fromCalendar.time = startDate!!
        fromCalendar[Calendar.HOUR_OF_DAY] = 0
        fromCalendar[Calendar.MINUTE] = 0
        fromCalendar[Calendar.SECOND] = 0
        fromCalendar[Calendar.MILLISECOND] = 0
        val toCalendar = Calendar.getInstance()
        toCalendar.time = endDate!!
        toCalendar[Calendar.HOUR_OF_DAY] = 0
        toCalendar[Calendar.MINUTE] = 0
        toCalendar[Calendar.SECOND] = 0
        toCalendar[Calendar.MILLISECOND] = 0
        return ((toCalendar.time.time - fromCalendar.time.time) / (1000 * 60 * 60 * 24)).toInt()
    }
}