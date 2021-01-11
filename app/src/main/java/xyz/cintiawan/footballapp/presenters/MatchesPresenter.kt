package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import xyz.cintiawan.footballapp.tests.CoroutineContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.MatchResponse
import xyz.cintiawan.footballapp.models.MatchSearchResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.MatchesView

class MatchesPresenter(private val view: MatchesView,
                       private val apiRepository: ApiRepository,
                       private val gson: Gson,
                       private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                       private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getPrevMatchesList(league: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getMatchesPrev(league)),
                        MatchResponse::class.java)
            }

            view.showMatchesList(data.await().events)
            view.hideLoading()
            idler.decrement()
        }
    }

    fun getNextMatchesList(league: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getMatchesNext(league)),
                        MatchResponse::class.java)
            }

            view.showMatchesList(data.await().events)
            view.hideLoading()
            idler.decrement()
        }
    }

    fun getMatchesSearch(team: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getMatchesSearch(team)),
                        MatchSearchResponse::class.java)
            }

            view.showMatchesList(data.await().event)
            view.hideLoading()
            idler.decrement()
        }
    }
}