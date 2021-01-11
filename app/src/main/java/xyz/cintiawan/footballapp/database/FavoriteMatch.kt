package xyz.cintiawan.footballapp.database

import xyz.cintiawan.footballapp.parseToCalendar
import java.text.SimpleDateFormat
import java.util.*

data class FavoriteMatch(val id: Long?,
                         val eventId: String?,
                         val date: String?,
                         val time: String?,
                         val homeTeam: String?,
                         val awayTeam: String?,
                         val homeScore: String?,
                         val awayScore: String?) {
    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE_MATCH"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val DATE: String = "DATE"
        const val TIME: String = "TIME"
        const val HOME_TEAM: String = "HOME_TEAM"
        const val AWAY_TEAM: String = "AWAY_TEAM"
        const val HOME_SCORE: String = "HOME_SCORE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
    }

    fun getDateTimeBegin(): Calendar {
        return (this.date + " " + this.time).parseToCalendar("EEE, dd MMM yyyy HH:mm")
    }

    fun getDateTimeEnd(): Calendar {
        val cal = getDateTimeBegin()
        cal.add(Calendar.HOUR_OF_DAY, 2)

        return cal
    }
}