package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.Team

interface TeamsView : MainView {
    fun showSpinner()
    fun hideSpinner()
    fun showTeamsList(data: List<Team>)
}