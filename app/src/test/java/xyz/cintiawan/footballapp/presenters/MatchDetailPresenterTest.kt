package xyz.cintiawan.footballapp.presenters

import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.idling.CountingIdlingResource
import com.google.gson.Gson
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import xyz.cintiawan.footballapp.tests.TestContextProvider
import xyz.cintiawan.footballapp.TheSportsDBApi
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.models.MatchResponse
import xyz.cintiawan.footballapp.models.Team
import xyz.cintiawan.footballapp.models.TeamResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.MatchDetailView

class MatchDetailPresenterTest {
    private lateinit var presenter: MatchDetailPresenter

    @Mock
    private
    lateinit var view: MatchDetailView

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
        presenter = MatchDetailPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testGetMatchDetail() {
        val events: MutableList<Match> = mutableListOf()
        val response = MatchResponse(events)
        val event = "576470"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getMatchDetail(event)),
                MatchResponse::class.java
        )).thenReturn(response)

        presenter.getMatchDetail(event)

        verify(view).showLoading()
        verify(view).showMatchDetail(events)
        verify(view).hideLoading()
    }

    @Test
    fun testGetTeamBadges() {
        val teamsHome: MutableList<Team> = mutableListOf()
        val responseHome = TeamResponse(teamsHome)
        val teamsAway: MutableList<Team> = mutableListOf()
        val responseAway = TeamResponse(teamsAway)
        val event = Mockito.mock(Match::class.java)

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getTeamDetail(event.homeTeamId)),
                TeamResponse::class.java
        )).thenReturn(responseHome)

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getTeamDetail(event.awayTeamId)),
                TeamResponse::class.java
        )).thenReturn(responseAway)

        presenter.getTeamBadges(event)

        verify(view).showHomeTeamBadge(teamsHome)
        verify(view).showAwayTeamBadge(teamsAway)
    }
}