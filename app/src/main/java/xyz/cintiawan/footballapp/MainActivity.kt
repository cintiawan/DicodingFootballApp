package xyz.cintiawan.footballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import xyz.cintiawan.footballapp.R.id.*
import xyz.cintiawan.footballapp.R.id.matches
import xyz.cintiawan.footballapp.fragments.FavoritesFragment
import xyz.cintiawan.footballapp.fragments.MatchesFragment
import xyz.cintiawan.footballapp.fragments.TeamsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                matches -> {
                    loadMatchesFragment(savedInstanceState)
                }
                teams -> {
                    loadTeamsFragment(savedInstanceState)
                }
                favorites -> {
                    loadFavoritesFragment(savedInstanceState)
                }
            }
            true
        }
        bottom_nav.selectedItemId = matches
    }

    private fun loadMatchesFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, MatchesFragment(), MatchesFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadTeamsFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, TeamsFragment(), TeamsFragment::class.simpleName)
                    .commit()
        }
    }

    private fun loadFavoritesFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, FavoritesFragment(), FavoritesFragment::class.simpleName)
                    .commit()
        }
    }
}
