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
import xyz.cintiawan.footballapp.views.TeamDetailView

class TeamDetailPresenterTest {
    private lateinit var presenter: TeamDetailPresenter

    @Mock
    private
    lateinit var view: TeamDetailView

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
        presenter = TeamDetailPresenter(view, apiRepository, gson, idlingResource, TestContextProvider())
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun testGetTeamDetail() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val team = "133604"

        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportsDBApi.getTeamDetail(team)),
                TeamResponse::class.java
        )).thenReturn(response)

        presenter.getTeamDetail(team)

        verify(view).showLoading()
        verify(view).showTeamDetail(teams)
        verify(view).hideLoading()
    }
}