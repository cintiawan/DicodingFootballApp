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
import xyz.cintiawan.footballapp.invisible
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.visible
import java.util.*

class MatchesAdapter(private val context: Context, private val matches: List<Match>, private val listener: (Match) -> Unit, private val alertListener: (Match) -> Unit)
    : RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>() {

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

        fun bindItem(matches: Match, listener: (Match) -> Unit, alertListener: (Match) -> Unit) {
            date.text = matches.getDateFormat()
            time.text = matches.getTimeFormat()
            homeTeam.text = matches.homeTeam
            awayTeam.text = matches.awayTeam
            homeScore.text = matches.homeScore
            awayScore.text = matches.awayScore

            if(matches.getDateTimeBegin() <= Calendar.getInstance())
                alertBtn.invisible()
            else {
                alertBtn.visible()
                alertBtn.onClick {
                    alertListener(matches)
                }
            }

            itemView.setOnClickListener {
                listener(matches)
            }
        }
    }
}