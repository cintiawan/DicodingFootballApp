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
import xyz.cintiawan.footballapp.models.Team
import xyz.cintiawan.footballapp.models.TeamResponse
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.tests.IdlingResourceProvider
import xyz.cintiawan.footballapp.views.TeamsView

class TeamsPresenterTest {
    private lateinit var presenter: TeamsPresenter

    @Mock
    private
    lateinit var view: TeamsView

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
        presenter = TeamsPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testGetTeamsList() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val league = "4328"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getTeams(league)),
                TeamResponse::class.java
        )).thenReturn(response)

        presenter.getTeamsList(league)

        verify(view).showLoading()
        verify(view).showTeamsList(teams)
        verify(view).hideLoading()
        verify(view).showSpinner()
    }

    @Test
    fun testGetTeamsSearch() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val team = "barcelona"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getTeamsSearch(team)),
                TeamResponse::class.java
        )).thenReturn(response)

        presenter.getTeamsSearch(team)

        verify(view).showLoading()
        verify(view).hideSpinner()
        verify(view).showTeamsList(teams)
        verify(view).hideLoading()
    }
}