package xyz.cintiawan.footballapp.fragments


import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_matches_list.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import xyz.cintiawan.footballapp.MatchDetailActivity

import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.adapters.MatchesAdapter
import xyz.cintiawan.footballapp.invisible
import xyz.cintiawan.footballapp.models.League
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.LeaguesPresenter
import xyz.cintiawan.footballapp.presenters.MatchesPresenter
import xyz.cintiawan.footballapp.views.LeagueView
import xyz.cintiawan.footballapp.views.MatchesView
import xyz.cintiawan.footballapp.visible

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NextMatchesFragment : Fragment(), MatchesView, LeagueView {
    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var presenter: MatchesPresenter
    private lateinit var leaguePresenter: LeaguesPresenter
    private lateinit var adapter: MatchesAdapter

    private var leagueId: String? = ""
    private var leagueIds: MutableList<String> = mutableListOf()
    private var leagueNames: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list_match.layoutManager = LinearLayoutManager(ctx)

        val request = ApiRepository()
        val gson = Gson()
        leaguePresenter = LeaguesPresenter(this, request, gson)
        leaguePresenter.getLeaguesList()

        val listener: (Match) -> Unit = { ctx.startActivity<MatchDetailActivity>(MatchDetailActivity.MATCH_ID to "${it.eventId}") }
        val alertListener: (Match) -> Unit = {
            val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, it.homeTeam + " vs " + it.awayTeam)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, it.getDateTimeBegin().timeInMillis)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, it.getDateTimeEnd().timeInMillis)
            startActivity(intent)
        }
        adapter = MatchesAdapter(ctx, matches, listener, alertListener)
        list_match.adapter = adapter

        presenter = MatchesPresenter(this, request, gson)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_matches_list, container, false)

    private fun action() {
        leagueId = leagueIds[spinner.selectedItemPosition]
        presenter.getNextMatchesList(leagueId)
    }

    override fun showMatchesList(data: List<Match>) {
        swipe_refresh.isRefreshing = false
        matches.clear()
        matches.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showLeaguesList(data: List<League>) {
        for((index, value) in data.withIndex()) {
            leagueIds.add(index, value.leagueId.toString())
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

    override fun showSpinner() {
        spinner.visible()
        swipe_refresh.isEnabled = true
    }

    override fun hideSpinner() {
        spinner.invisible()
        swipe_refresh.isEnabled = false
    }

}
