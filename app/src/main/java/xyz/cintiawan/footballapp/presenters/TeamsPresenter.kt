package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import xyz.cintiawan.footballapp.tests.CoroutineContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.TeamResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.TeamsView

class TeamsPresenter(private val view: TeamsView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson,
                     private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                     private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getTeamsList(league: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getTeams(league)),
                        TeamResponse::class.java)
            }

            view.showTeamsList(data.await().teams)
            view.hideLoading()
            view.showSpinner()
            idler.decrement()
        }
    }

    fun getTeamsSearch(team: String?) {
        view.showLoading()
        view.hideSpinner()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getTeamsSearch(team)),
                        TeamResponse::class.java)
            }

            view.showTeamsList(data.await().teams)
            view.hideLoading()
            idler.decrement()
        }
    }
}