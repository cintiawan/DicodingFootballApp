package xyz.cintiawan.footballapp.models

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.footballapp.parseToCalendar
import xyz.cintiawan.footballapp.toSimpleDate
import xyz.cintiawan.footballapp.toSimpleTime
import java.text.SimpleDateFormat
import java.util.*

data class Match(
        @SerializedName("idEvent")
        var eventId: String?,

        @SerializedName("strDate")
        var date: String?,

        @SerializedName("strTime")
        var time: String?,

        @SerializedName("idHomeTeam")
        var homeTeamId: String?,

        @SerializedName("idAwayTeam")
        var awayTeamId: String?,

        @SerializedName("strHomeTeam")
        var homeTeam: String?,

        @SerializedName("strAwayTeam")
        var awayTeam: String?,

        @SerializedName("intHomeScore")
        var homeScore: String?,

        @SerializedName("intAwayScore")
        var awayScore: String?,

        @SerializedName("strHomeFormation")
        var homeFormation: String?,

        @SerializedName("strAwayFormation")
        var awayFormation: String?,

        @SerializedName("strHomeGoalDetails")
        var homeGoalDetail: String?,

        @SerializedName("strAwayGoalDetails")
        var awayGoalDetail: String?,

        @SerializedName("intHomeShots")
        var homeShots: String?,

        @SerializedName("intAwayShots")
        var awayShots: String?,

        @SerializedName("strHomeLineupGoalkeeper")
        var homeGoalkeeper: String?,

        @SerializedName("strAwayLineupGoalkeeper")
        var awayGoalkeeper: String?,

        @SerializedName("strHomeLineupDefense")
        var homeDefense: String?,

        @SerializedName("strAwayLineupDefense")
        var awayDefense: String?,

        @SerializedName("strHomeLineupMidfield")
        var homeMidfield: String?,

        @SerializedName("strAwayLineupMidfield")
        var awayMidfield: String?,

        @SerializedName("strHomeLineupForward")
        var homeForward: String?,

        @SerializedName("strAwayLineupForward")
        var awayForward: String?,

        @SerializedName("strHomeLineupSubstitutes")
        var homeSubtitutes: String?,

        @SerializedName("strAwayLineupSubstitutes")
        var awaySubtitutes: String?
) {
    fun getDateTimeBegin(): Calendar {
        return (this.date + " " + this.time).parseToCalendar("dd/MM/yy HH:mm:ss")
    }

    fun getDateTimeEnd(): Calendar {
        val cal = getDateTimeBegin()
        cal.add(Calendar.HOUR_OF_DAY, 2)

        return cal
    }

    fun getDateFormat(): String {
        return getDateTimeBegin().time.toSimpleDate()
    }

    fun getTimeFormat(): String {
        return getDateTimeBegin().time.toSimpleTime()
    }
}