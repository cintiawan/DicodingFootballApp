package xyz.cintiawan.footballapp.views

import xyz.cintiawan.footballapp.models.Player

interface PlayersView : MainView {
    fun showPlayersList(data: List<Player>)
}