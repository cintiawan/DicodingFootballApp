package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import xyz.cintiawan.footballapp.tests.TestContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.Player
import xyz.cintiawan.footballapp.models.PlayerDetailResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.PlayerDetailView

class PlayerDetailPresenterTest {
    private lateinit var presenter: PlayerDetailPresenter

    @Mock
    private
    lateinit var view: PlayerDetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Mock
    private
    lateinit var idlingResource: CountingIdlingResource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PlayerDetailPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testGetPlayerDetail() {
        val players: MutableList<Player> = mutableListOf()
        val response = PlayerDetailResponse(players)
        val player = "34145395"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getPlayerDetail(player)),
                PlayerDetailResponse::class.java
        )).thenReturn(response)

        presenter.getPlayerDetail(player)

        verify(view).showLoading()
        verify(view).showPlayerDetail(players)
        verify(view).hideLoading()
    }
}