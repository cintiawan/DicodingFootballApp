package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import xyz.cintiawan.footballapp.tests.CoroutineContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.models.MatchResponse
import xyz.cintiawan.footballapp.models.TeamResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.MatchDetailView

class MatchDetailPresenter(private val view: MatchDetailView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson,
                           private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                           private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getMatchDetail(id: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getMatchDetail(id)),
                        MatchResponse::class.java)
            }

            view.showMatchDetail(data.await().events)
            view.hideLoading()
            idler.decrement()
        }
    }

    fun getTeamBadges(match: Match) {
        async(context.main) {
            idler.increment()
            val home = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getTeamDetail(match.homeTeamId)),
                        TeamResponse::class.java)
            }

            val away = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getTeamDetail(match.awayTeamId)),
                        TeamResponse::class.java)
            }

            view.showHomeTeamBadge(home.await().teams)
            view.showAwayTeamBadge(away.await().teams)
            idler.decrement()
        }
    }
}