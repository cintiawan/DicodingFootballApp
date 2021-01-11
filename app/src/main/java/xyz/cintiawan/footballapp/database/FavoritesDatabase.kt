package xyz.cintiawan.footballapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class FavoritesDatabase(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorites.db", null, 1) {
    companion object {
        private var instance: FavoritesDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): FavoritesDatabase {
            if (instance == null) {
                instance = FavoritesDatabase(ctx.applicationContext)
            }
            return instance as FavoritesDatabase
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(FavoriteMatch.TABLE_FAVORITE, true,
                FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteMatch.EVENT_ID to TEXT + UNIQUE,
                FavoriteMatch.DATE to TEXT,
                FavoriteMatch.TIME to TEXT,
                FavoriteMatch.HOME_TEAM to TEXT,
                FavoriteMatch.AWAY_TEAM to TEXT,
                FavoriteMatch.HOME_SCORE to TEXT,
                FavoriteMatch.AWAY_SCORE to TEXT)

        db.createTable(FavoriteTeam.TABLE_FAVORITE, true,
                FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
                FavoriteTeam.TEAM_NAME to TEXT,
                FavoriteTeam.TEAM_BADGE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteMatch.TABLE_FAVORITE, true)
        db.dropTable(FavoriteTeam.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.matchDatabase: FavoritesDatabase
    get() = FavoritesDatabase.getInstance(applicationContext)

val Context.teamDatabase: FavoritesDatabase
    get() = FavoritesDatabase.getInstance(applicationContext)