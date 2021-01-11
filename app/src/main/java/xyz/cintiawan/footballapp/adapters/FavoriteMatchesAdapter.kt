package xyz.cintiawan.footballapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.database.FavoriteMatch
import xyz.cintiawan.footballapp.invisible
import xyz.cintiawan.footballapp.visible
import java.util.*

class FavoriteMatchesAdapter(private val context: Context, private val matches: List<FavoriteMatch>, private val listener: (FavoriteMatch) -> Unit, private val alertListener: (FavoriteMatch) -> Unit)
    : RecyclerView.Adapter<FavoriteMatchesAdapter.MatchesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MatchesViewHolder(LayoutInflater.from(context).inflate(R.layout.match_list, parent, false))

    override fun getItemCount(): Int = matches.size

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.bindItem(matches[position], listener, alertListener)
    }

    class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.find(R.id.date)
        private val time: TextView = view.find(R.id.time)
        private val homeTeam: TextView = view.find(R.id.home_team)
        private val homeScore: TextView = view.find(R.id.home_score)
        private val awayTeam: TextView = view.find(R.id.away_team)
        private val awayScore: TextView = view.find(R.id.away_score)
        private val alertBtn: ImageButton = view.find(R.id.alert)

        fun bindItem(favorites: FavoriteMatch, listener: (FavoriteMatch) -> Unit, alertListener: (FavoriteMatch) -> Unit) {
            date.text = favorites.date
            time.text = favorites.time
            homeTeam.text = favorites.homeTeam
            awayTeam.text = favorites.awayTeam
            homeScore.text = favorites.homeScore
            awayScore.text = favorites.awayScore

            if(favorites.getDateTimeBegin().timeInMillis <= Calendar.getInstance().timeInMillis)
                alertBtn.invisible()
            else {
                alertBtn.visible()
                alertBtn.onClick {
                    alertListener(favorites)
                }
            }

            itemView.setOnClickListener {
                listener(favorites)
            }
        }
    }
}