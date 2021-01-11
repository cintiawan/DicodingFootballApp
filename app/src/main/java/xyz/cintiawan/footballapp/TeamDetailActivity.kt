package xyz.cintiawan.footballapp

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import xyz.cintiawan.footballapp.R.drawable.ic_add_to_favorite
import xyz.cintiawan.footballapp.R.drawable.ic_added_to_favorite
import xyz.cintiawan.footballapp.R.id.add_to_favorite
import xyz.cintiawan.footballapp.R.menu.detail_menu
import xyz.cintiawan.footballapp.adapters.ViewPagerAdapter
import xyz.cintiawan.footballapp.database.FavoriteTeam
import xyz.cintiawan.footballapp.database.teamDatabase
import xyz.cintiawan.footballapp.fragments.TeamDescriptionFragment
import xyz.cintiawan.footballapp.fragments.TeamPlayerFragment
import xyz.cintiawan.footballapp.models.Team
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.TeamDetailPresenter
import xyz.cintiawan.footballapp.views.TeamDetailView

class TeamDetailActivity : AppCompatActivity(), TeamDetailView {
    companion object {
        const val TEAM_ID = "team_id"
    }
    private lateinit var presenter: TeamDetailPresenter
    private lateinit var teams: Team
    private lateinit var id: String
    private lateinit var adapter: ViewPagerAdapter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        id = intent.getStringExtra(TEAM_ID)

        favoriteState()
        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamDetailPresenter(this, request, gson)
        presenter.getTeamDetail(id)
    }

    override fun showLoading() {
        tabs.invisible()
        progress_bar.visible()
    }

    override fun hideLoading() {
        tabs.visible()
        progress_bar.invisible()
    }

    override fun showTeamDetail(data: List<Team>) {
        teams = Team(data[0].teamId,
                data[0].teamName,
                data[0].teamBadge)
        Picasso.get().load(data[0].teamBadge).into(team_badge)
        team_name.text = data[0].teamName
        team_year.text = data[0].teamFormedYear
        team_stadium.text = data[0].teamStadium

        setupViewPager(data[0])
    }

    private fun setupViewPager(team: Team) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TeamDescriptionFragment.newInstance(team.teamDescription))
        adapter.addFragment(TeamPlayerFragment.newInstance(team.teamId))
        viewpager.adapter = adapter

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.currentItem = tab.position
            }
        })
    }

    private fun addToFavorite(){
        try {
            teamDatabase.use {
                insert(FavoriteTeam.TABLE_FAVORITE,
                        FavoriteTeam.TEAM_ID to teams.teamId,
                        FavoriteTeam.TEAM_NAME to teams.teamName,
                        FavoriteTeam.TEAM_BADGE to teams.teamBadge)
            }
            snackbar(container, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(container, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            teamDatabase.use {
                delete(FavoriteTeam.TABLE_FAVORITE, "(TEAM_ID = {id})",
                        "id" to id)
            }
            snackbar(container, "Removed from favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(container, e.localizedMessage).show()
        }
    }

    private fun favoriteState(){
        teamDatabase.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to id)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorite)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorite)
    }
}
