package xyz.cintiawan.footballapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.R.id.player_name
import xyz.cintiawan.footballapp.R.id.player_photo
import xyz.cintiawan.footballapp.models.Player

class PlayersAdapter (private val context: Context, private val players: List<Player>, private val listener: (Player) -> Unit)
    : RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
            PlayerViewHolder(LayoutInflater.from(context).inflate(R.layout.player_list, parent, false))

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) = holder.bindItem(players[position], listener)

    class PlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val playerPhoto: ImageView = view.find(player_photo)
        private val playerName: TextView = view.find(player_name)

        fun bindItem(players: Player, listener: (Player) -> Unit) {
            Picasso.get().load(players.playerPhoto).into(playerPhoto)
            playerName.text = players.playerName
            itemView.onClick { listener(players) }
        }
    }
}