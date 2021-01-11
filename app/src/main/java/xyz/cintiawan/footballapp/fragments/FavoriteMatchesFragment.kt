package xyz.cintiawan.footballapp.fragments


import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_favorite_matches.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import xyz.cintiawan.footballapp.MatchDetailActivity

import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.adapters.FavoriteMatchesAdapter
import xyz.cintiawan.footballapp.database.FavoriteMatch
import xyz.cintiawan.footballapp.database.matchDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoriteMatchesFragment : Fragment() {
    private var favorites: MutableList<FavoriteMatch> = mutableListOf()
    private lateinit var adapter: FavoriteMatchesAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listener: (FavoriteMatch) -> Unit = { ctx.startActivity<MatchDetailActivity>(MatchDetailActivity.MATCH_ID to "${it.eventId}") }
        val alertListener: (FavoriteMatch) -> Unit = {
            val intent = Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, it.homeTeam + " vs " + it.awayTeam)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, it.getDateTimeBegin().timeInMillis)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, it.getDateTimeEnd().timeInMillis)
            startActivity(intent)
        }
        adapter = FavoriteMatchesAdapter(ctx, favorites, listener, alertListener)

        list_match.layoutManager = LinearLayoutManager(ctx)
        list_match.adapter = adapter

        showFavorite()
        swipe_refresh.onRefresh {
            favorites.clear()
            showFavorite()
            val toast = Toast.makeText(ctx, "Data refreshed", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_favorite_matches, container, false)

    private fun showFavorite(){
        ctx.matchDatabase.use {
            swipe_refresh.isRefreshing = false
            val result = select(FavoriteMatch.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }
}
