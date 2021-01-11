package xyz.cintiawan.footballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player_detail.*
import xyz.cintiawan.footballapp.models.Player
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.PlayerDetailPresenter
import xyz.cintiawan.footballapp.views.PlayerDetailView

class PlayerDetailActivity : AppCompatActivity(), PlayerDetailView {
    private lateinit var presenter: PlayerDetailPresenter
    private lateinit var player: Player
    private lateinit var id: String

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nested_scroll_view.isFillViewport = true

        id = intent.getStringExtra("id")

        val request = ApiRepository()
        val gson = Gson()
        presenter = PlayerDetailPresenter(this, request, gson)
        presenter.getPlayerDetail(id)
    }

    override fun showPlayerDetail(data: List<Player>) {
        player =  data[0]
        Picasso.get().load(player.playerHeader).into(player_banner)
        supportActionBar?.title = player.playerName
        player_weight.text = player.playerWeight
        player_height.text = player.playerHeight
        player_position.text = player.playerPosition
        player_description.text = player.playerDescription
    }

    override fun showLoading() {
        progress_bar.visible()
    }

    override fun hideLoading() {
        progress_bar.invisible()
    }
}
