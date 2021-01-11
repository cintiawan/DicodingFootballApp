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
import xyz.cintiawan.footballapp.models.PlayerResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.PlayersView

class PlayersPresenterTest {
    private lateinit var presenter: PlayersPresenter

    @Mock
    private
    lateinit var view: PlayersView

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
        presenter = PlayersPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testGetPlayersList() {
        val players: MutableList<Player> = mutableListOf()
        val response = PlayerResponse(players)
        val team = "133604"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getPlayers(team)),
                PlayerResponse::class.java
        )).thenReturn(response)

        presenter.getPlayersList(team)

        verify(view).showLoading()
        verify(view).showPlayersList(players)
        verify(view).hideLoading()
    }
}