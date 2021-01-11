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
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.models.MatchResponse
import xyz.cintiawan.footballapp.models.MatchSearchResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.views.MatchesView

class MatchesPresenterTest {
    private lateinit var presenter: MatchesPresenter

    @Mock
    private
    lateinit var view: MatchesView

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
        presenter = MatchesPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testGetPrevMatchesList() {
        val events: MutableList<Match> = mutableListOf()
        val response = MatchResponse(events)
        val league = "4328"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getMatchesPrev(league)),
                MatchResponse::class.java
        )).thenReturn(response)

        presenter.getPrevMatchesList(league)

        verify(view).showLoading()
        verify(view).showMatchesList(events)
        verify(view).hideLoading()
    }

    @Test
    fun testGetNextMatchesList() {
        val events: MutableList<Match> = mutableListOf()
        val response = MatchResponse(events)
        val league = "4328"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getMatchesNext(league)),
                MatchResponse::class.java
        )).thenReturn(response)

        presenter.getNextMatchesList(league)

        verify(view).showLoading()
        verify(view).showMatchesList(events)
        verify(view).hideLoading()
    }

    @Test
    fun testGetMatchesSearch() {
        val events: MutableList<Match> = mutableListOf()
        val response = MatchSearchResponse(events)
        val team = "barcelona"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getMatchesSearch(team)),
                MatchSearchResponse::class.java
        )).thenReturn(response)

        presenter.getMatchesSearch(team)

        verify(view).showLoading()
        verify(view).showMatchesList(events)
        verify(view).hideLoading()
    }
}