package xyz.cintiawan.footballapp

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.design.snackbar
import xyz.cintiawan.footballapp.R.drawable.ic_add_to_favorite
import xyz.cintiawan.footballapp.R.drawable.ic_added_to_favorite
import xyz.cintiawan.footballapp.R.id.add_to_favorite
import xyz.cintiawan.footballapp.R.menu.detail_menu
import xyz.cintiawan.footballapp.database.FavoriteMatch
import xyz.cintiawan.footballapp.database.matchDatabase
import xyz.cintiawan.footballapp.models.Match
import xyz.cintiawan.footballapp.models.Team
import xyz.cintiawan.footballapp.network.ApiRepository
import xyz.cintiawan.footballapp.presenters.MatchDetailPresenter
import xyz.cintiawan.footballapp.views.MatchDetailView

class MatchDetailActivity : AppCompatActivity(), MatchDetailView {
    companion object {
        const val MATCH_ID = "match_id"
    }
    private val request = ApiRepository()
    private val gson = Gson()
    private var matches: MutableList<Match> = mutableListOf()
    private lateinit var matchId: String
    private lateinit var presenter: MatchDetailPresenter

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
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
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        matchId = intent.getStringExtra(MATCH_ID)

        favoriteState()
        presenter = MatchDetailPresenter(this, request, gson)
        presenter.getMatchDetail(matchId)
    }

    override fun showMatchDetail(data: List<Match>) {
        matches.addAll(data)
        val match = matches[0]
        fetchTeamBadge(match)

        date.text = match.getDateFormat()
        time.text = match.getTimeFormat()
        home_score.text = match.homeScore
        away_score.text = match.awayScore
        home_team.text = match.homeTeam
        away_team.text = match.awayTeam
        home_formation.text = match.homeFormation
        away_formation.text = match.awayFormation
        home_goal_detail.text = match.homeGoalDetail?.replace(';', '\n')
        away_goal_detail.text = match.awayGoalDetail?.replace(';', '\n')
        home_shots.text = match.homeShots
        away_shots.text = match.awayShots
        home_gk.text = match.homeGoalkeeper
        away_gk.text = match.awayGoalkeeper
        home_def.text = match.homeDefense?.replace(';', '\n')
        away_def.text =  match.awayDefense?.replace(';', '\n')
        home_mid.text = match.homeMidfield?.replace(';', '\n')
        away_mid.text = match.awayMidfield?.replace(';', '\n')
        home_fwd.text = match.homeForward?.replace(';', '\n')
        away_fwd.text = match.awayForward?.replace(';', '\n')
        home_sub.text = match.homeSubtitutes?.replace(';', '\n')
        away_sub.text = match.awaySubtitutes?.replace(';', '\n')
    }

    private fun fetchTeamBadge(match: Match) {
        presenter = MatchDetailPresenter(this, request, gson)
        presenter.getTeamBadges(match)
    }

    override fun showHomeTeamBadge(data: List<Team>) {
        Picasso.get().load(data[0].teamBadge).into(home_badge)
    }

    override fun showAwayTeamBadge(data: List<Team>) {
        Picasso.get().load(data[0].teamBadge).into(away_badge)
    }

    override fun showLoading() {
        progressBar.visible()
        container.invisible()
    }

    override fun hideLoading() {
        progressBar.invisible()
        container.visible()
    }

    private fun addToFavorite() {
        try {
            matchDatabase.use {
                insert(FavoriteMatch.TABLE_FAVORITE,
                        FavoriteMatch.EVENT_ID to matchId,
                        FavoriteMatch.DATE to matches[0].getDateFormat(),
                        FavoriteMatch.TIME to matches[0].getTimeFormat(),
                        FavoriteMatch.HOME_TEAM to matches[0].homeTeam,
                        FavoriteMatch.AWAY_TEAM to matches[0].awayTeam,
                        FavoriteMatch.HOME_SCORE to matches[0].homeScore,
                        FavoriteMatch.AWAY_SCORE to matches[0].awayScore)
            }
            snackbar(container, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(container, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            matchDatabase.use {
                delete(FavoriteMatch.TABLE_FAVORITE, "(EVENT_ID = {id})",
                        "id" to matchId)
            }
            snackbar(container, "Removed from favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(container, e.localizedMessage).show()
        }
    }

    private fun favoriteState() {
        matchDatabase.use {
            val result = select(FavoriteMatch.TABLE_FAVORITE)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to matchId)
            val favorite = result.parseList(classParser<FavoriteMatch>())
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
