package xyz.cintiawan.footballapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_team_player.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import xyz.cintiawan.footballapp.PlayerDetailActivity

import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.adapters.PlayersAdapter
import xyz.cintiawan.footballapp.invisible
import xyz.cintiawan.footballapp.models.Player
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.PlayersPresenter
import xyz.cintiawan.footballapp.views.PlayersView
import xyz.cintiawan.footballapp.visible

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TeamPlayerFragment : Fragment(), PlayersView {
    private var players: MutableList<Player> = mutableListOf()
    private lateinit var presenter: PlayersPresenter
    private lateinit var adapter: PlayersAdapter

    companion object {
        private const val TEAM = "team"
        fun newInstance(team: String?): TeamPlayerFragment {
            val fragment = TeamPlayerFragment()
            val args = Bundle()
            args.putString(TEAM, team)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        list_player.layoutManager = LinearLayoutManager(ctx)

        adapter = PlayersAdapter(ctx, players) {
            ctx.startActivity<PlayerDetailActivity>("id" to "${it.playerId}")
        }
        list_player.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = PlayersPresenter(this, request, gson)

        val team = arguments?.getString(TEAM)
        presenter.getPlayersList(team)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_team_player, container, false)


    override fun showPlayersList(data: List<Player>) {
        players.clear()
        players.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        progress_bar.visible()
    }

    override fun hideLoading() {
        progress_bar.invisible()
    }
}
