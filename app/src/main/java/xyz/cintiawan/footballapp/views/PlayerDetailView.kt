package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.Player

interface PlayerDetailView : MainView {
    fun showPlayerDetail(data: List<Player>)
}