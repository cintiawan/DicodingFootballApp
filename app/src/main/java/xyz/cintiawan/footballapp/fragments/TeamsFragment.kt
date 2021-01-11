package xyz.cintiawan.footballapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_teams.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import xyz.cintiawan.footballapp.*

import xyz.cintiawan.footballapp.R.id.action_search
import xyz.cintiawan.footballapp.adapters.TeamsAdapter
import xyz.cintiawan.footballapp.models.League
import xyz.cintiawan.footballapp.models.Team
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.LeaguesPresenter
import xyz.cintiawan.footballapp.presenters.TeamsPresenter
import xyz.cintiawan.footballapp.views.LeagueView
import xyz.cintiawan.footballapp.views.TeamsView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TeamsFragment : Fragment(), TeamsView, LeagueView {
    private var teams: MutableList<Team> = mutableListOf()
    private lateinit var presenter: TeamsPresenter
    private lateinit var leaguePresenter: LeaguesPresenter
    private lateinit var adapter: TeamsAdapter

    private lateinit var leagueName: String
    private var leagueNames: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        list_team.layoutManager = LinearLayoutManager(ctx)

        val request = ApiRepository()
        val gson = Gson()
        leaguePresenter = LeaguesPresenter(this, request, gson)
        leaguePresenter.getLeaguesList()

        adapter = TeamsAdapter(ctx, teams) {
            ctx.startActivity<TeamDetailActivity>(TeamDetailActivity.TEAM_ID to "${it.teamId}")
        }
        list_team.adapter = adapter

        presenter = TeamsPresenter(this, request, gson)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action()
            }
        }

        swipe_refresh.onRefresh {
            action()
            hideLoading()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.action_bar, menu)

        val searchView = menu?.findItem(action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if(!newText.isEmpty()) presenter.getTeamsSearch(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean { return false }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_teams, container, false)

    private fun action() {
        leagueName = spinner.selectedItem.toString()
        presenter.getTeamsList(leagueName)
    }

    override fun showSpinner() {
        spinner.visible()
        swipe_refresh.isEnabled = true
    }

    override fun hideSpinner() {
        spinner.gone()
        swipe_refresh.isEnabled = false
    }

    override fun showTeamsList(data: List<Team>) {
        swipe_refresh.isRefreshing = false
        teams.clear()
        teams.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showLeaguesList(data: List<League>) {
        for((index, value) in data.withIndex()) {
            leagueNames.add(index, value.leagueName.toString())
        }
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, leagueNames)
        spinner.adapter = spinnerAdapter
    }

    override fun showLoading() {
        progress_bar.visible()
    }

    override fun hideLoading() {
        progress_bar.invisible()
    }

}
