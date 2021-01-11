package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import xyz.cintiawan.footballapp.tests.CoroutineContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.PlayerDetailResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.PlayerDetailView

class PlayerDetailPresenter(private val view: PlayerDetailView,
                            private val apiRepository: ApiRepository,
                            private val gson: Gson,
                            private val idler: CountingIdlingResource = IdlingResourceProvider.idler,
                            private val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getPlayerDetail(player: String?) {
        view.showLoading()

        async(context.main) {
            idler.increment()
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportsDBApi.getPlayerDetail(player)),
                        PlayerDetailResponse::class.java)
            }

            view.showPlayerDetail(data.await().players)
            view.hideLoading()
            idler.decrement()
        }
    }
}