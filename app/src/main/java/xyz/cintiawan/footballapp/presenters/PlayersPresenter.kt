package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import xyz.cintiawan.footballapp.tests.CoroutineContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.PlayerResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.PlayersView

class PlayersPresenter(private val view: PlayersView,
                       private val apiRepository: ApiRepository,
                       private val gson: Gson,
                       private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                       private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getPlayersList(team: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getPlayers(team)),
                        PlayerResponse::class.java)
            }

            view.showPlayersList(data.await().player)
            view.hideLoading()
            idler.decrement()
        }
    }
}