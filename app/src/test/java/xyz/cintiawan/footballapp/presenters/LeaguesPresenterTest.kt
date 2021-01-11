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
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.League
import xyz.cintiawan.footballapp.models.LeagueResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.TestContextProvider
import xyz.cintiawan.footballapp.views.LeagueView

class LeaguesPresenterTest {
    private lateinit var presenter: LeaguesPresenter

    @Mock
    private
    lateinit var view: LeagueView

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
        presenter = LeaguesPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun getLeaguesList() {
        val leagues: MutableList<League> = mutableListOf()
        val response = LeagueResponse(leagues)

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getLeagues()),
                LeagueResponse::class.java
        )).thenReturn(response)

        presenter.getLeaguesList()

        verify(view).showLoading()
        verify(view).hideSpinner()
        verify(view).showLeaguesList(leagues)
        verify(view).showSpinner()
        verify(view).hideLoading()
    }
}