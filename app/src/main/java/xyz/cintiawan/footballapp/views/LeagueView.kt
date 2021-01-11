package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.League

interface LeagueView : MainView {
    fun showSpinner()
    fun hideSpinner()
    fun showLeaguesList(data: List<League>)
}