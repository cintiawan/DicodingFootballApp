package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.LeagueResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.CoroutineContextProvider
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.LeagueView

class LeaguesPresenter(private val view: LeagueView,
                       private val apiRepository: ApiRepository,
                       private val gson: Gson,
                       private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                       private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getLeaguesList() {
        view.showLoading()
        view.hideSpinner()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getLeagues()),
                        LeagueResponse::class.java)
            }

            view.showLeaguesList(data.await().countrys)
            view.hideLoading()
            view.showSpinner()
            idler.decrement()
        }
    }
}