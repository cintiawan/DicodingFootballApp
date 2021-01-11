package xyz.cintiawan.footballapp

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class UtilsKtTest {

    @Test
    fun testToSimpleDate() {
        assertEquals("Fri, 17 Aug 2018", ("17/08/2018" + " " + "11:30:00+00:00").parseToCalendar("dd/MM/yy HH:mm:ss").time.toSimpleDate())
    }

    @Test
    fun testToSimpleTime() {
        assertEquals("18:30", ("17/08/2018" + " " + "11:30:00+00:00").parseToCalendar("dd/MM/yy HH:mm:ss").time.toSimpleTime())
    }

    @Test
    fun testParseToCalendar() {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.ENGLISH)
        cal.time = sdf.parse("17/08/2018 18:30:00+00:00")

        assertEquals(cal.timeInMillis, ("17/08/2018 11:30:00+00:00").parseToCalendar("dd/MM/yy HH:mm:ss").timeInMillis)
    }
}