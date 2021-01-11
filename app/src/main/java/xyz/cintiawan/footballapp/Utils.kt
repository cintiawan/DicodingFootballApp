package xyz.cintiawan.footballapp

import android.util.Log
import android.view.View
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Date.toSimpleDate() : String {
    val format = SimpleDateFormat("EEE, dd MMM yyyy")
    return format.format(this)
}

fun Date.toSimpleTime() : String {
    val format = SimpleDateFormat("HH:mm")
    return format.format(this)
}

fun String.parseToCalendar(format: String) : Calendar {
    val cal = Calendar.getInstance()
    try {
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        cal.time = sdf.parse(this)
    } catch (e: ParseException) {
        cal.timeInMillis = 0
        Log.e("parse error", this)
    }

    return cal
}