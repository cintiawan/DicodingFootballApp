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
import xyz.cintiawan.footballapp.R.id.team_badge
import xyz.cintiawan.footballapp.R.id.team_name
import xyz.cintiawan.footballapp.models.Team

class TeamsAdapter(private val context: Context, private val teams: List<Team>, private val listener: (Team) -> Unit)
    : RecyclerView.Adapter<TeamsAdapter.TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder =
            TeamViewHolder(LayoutInflater.from(context).inflate(R.layout.team_list, parent, false))

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) = holder.bindItem(teams[position], listener)

    class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val teamBadge: ImageView = view.find(team_badge)
        private val teamName: TextView = view.find(team_name)

        fun bindItem(teams: Team, listener: (Team) -> Unit) {
            Picasso.get().load(teams.teamBadge).into(teamBadge)
            teamName.text = teams.teamName
            itemView.onClick { listener(teams) }
        }
    }
}