package xyz.cintiawan.footballapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search_match.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import xyz.cintiawan.footballapp.adapters.MatchesAdapter
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.MatchesPresenter
import xyz.cintiawan.footballapp.views.MatchesView

class SearchMatchActivity : AppCompatActivity(), MatchesView {
    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var presenter: MatchesPresenter
    private lateinit var adapter: MatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_match)
        setSupportActionBar(toolbar)

        search_view.requestFocus()
        list_match.layoutManager = LinearLayoutManager(ctx)

        val listener: (Match) -> Unit = { startActivity<MatchDetailActivity>(MatchDetailActivity.MATCH_ID to "${it.eventId}") }
        val alerListener: (Match) -> Unit = {
            val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, it.homeTeam + " vs " + it.awayTeam)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, it.getDateTimeBegin().timeInMillis)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, it.getDateTimeEnd().timeInMillis)
            startActivity(intent)
        }
        adapter = MatchesAdapter(ctx, matches, listener, alerListener)
        list_match.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = MatchesPresenter(this, request, gson)

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if(!newText.isEmpty()) presenter.getMatchesSearch(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean { return false }
        })
    }

    override fun showMatchesList(data: List<Match>) {
        matches.clear()
        matches.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        progress_bar.visible()
    }

    override fun hideLoading() {
        progress_bar.invisible()
    }
}
