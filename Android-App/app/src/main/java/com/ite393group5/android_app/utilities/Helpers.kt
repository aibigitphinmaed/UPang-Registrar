package com.ite393group5.android_app.utilities

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
object MySelectableDates : SelectableDates {
    /**
     * Returns true if the date item representing the [utcTimeMillis] should be enabled for
     * selection in the UI.
     */
   override fun isSelectableDate(utcTimeMillis: Long) : Boolean{
        val dayOfWeek = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC"))
            .toLocalDate().dayOfWeek
        return dayOfWeek != DayOfWeek.SUNDAY && dayOfWeek != DayOfWeek.SATURDAY && utcTimeMillis > System.currentTimeMillis()



   }

    /**
     * Returns true if a given [year] should be enabled for selection in the UI. When a year is
     * defined as non selectable, all the dates in that year will also be non selectable.
     */
    override fun isSelectableYear(year: Int):Boolean {
        return year >= LocalDate.now().year
    }
}

fun convertMillisToLocalDate(millis: Long): String {
    if(millis == 0L){
        return ""
    }
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertStringDateToMillis(date: String): Long? {
    if(date.isBlank()){
        return null
    }else{
        Timber.tag("Helpers").d("convertStringDateToMillis: $date")
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val parsedDate = formatter.parse(date)
        return parsedDate?.time
    }
}