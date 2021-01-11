package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.Match

interface MatchesView : MainView {
    fun showMatchesList(data: List<Match>)
}