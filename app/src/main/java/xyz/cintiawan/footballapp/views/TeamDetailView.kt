package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.Team

interface TeamDetailView : MainView {
    fun showTeamDetail(data: List<Team>)
}