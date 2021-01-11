package xyz.cintiawan.footballapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_favorite_teams.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh

import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.TeamDetailActivity
import xyz.cintiawan.footballapp.adapters.FavoriteTeamsAdapter
import xyz.cintiawan.footballapp.database.FavoriteTeam
import xyz.cintiawan.footballapp.database.teamDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoriteTeamsFragment : Fragment() {
    private var favorites: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var adapter: FavoriteTeamsAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listener: (FavoriteTeam) -> Unit = { ctx.startActivity<TeamDetailActivity>(TeamDetailActivity.TEAM_ID to "${it.teamId}") }
        adapter = FavoriteTeamsAdapter(ctx, favorites, listener)

        list_team.layoutManager = LinearLayoutManager(ctx)
        list_team.adapter = adapter

        showFavorite()
        swipe_refresh.onRefresh {
            favorites.clear()
            showFavorite()
            val toast = Toast.makeText(ctx, "Data refreshed", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_favorite_teams, container, false)

    private fun showFavorite(){
        ctx.teamDatabase.use {
            swipe_refresh.isRefreshing = false
            val result = select(FavoriteTeam.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }

}
