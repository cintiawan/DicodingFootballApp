package xyz.cintiawan.footballapp.tests

import android.support.test.espresso.idling.CountingIdlingResource

class IdlingResourceProvider {
    companion object {
        val idler: CountingIdlingResource by lazy { CountingIdlingResource("NETWORK_CALLS") }
    }
}