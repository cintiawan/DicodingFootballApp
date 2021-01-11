package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.models.Team

interface MatchDetailView : MainView {
    fun showMatchDetail(data: List<Match>)
    fun showHomeTeamBadge(data: List<Team>)
    fun showAwayTeamBadge(data: List<Team>)
}