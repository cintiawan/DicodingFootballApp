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
import xyz.cintiawan.footballapp.views.TeamDetailView

class TeamDetailPresenter(private val view: TeamDetailView,
                          private val apiRepository: ApiRepository,
                          private val gson: Gson,
                          private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                          private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getTeamDetail(team: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getTeamDetail(team)),
                        TeamResponse::class.java)
            }

            view.showTeamDetail(data.await().teams)
            view.hideLoading()
            idler.decrement()
        }
    }
}