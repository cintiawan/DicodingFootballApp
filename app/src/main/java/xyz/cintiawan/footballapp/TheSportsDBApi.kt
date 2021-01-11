package xyz.cintiawan.footballapp

object TheSportsDBApi {
    fun getBasePath(): String {
        return BuildConfig.BASE_URL + "api/v1/json/" + BuildConfig.TSDB_API_KEY + "/"
    }

    fun getMatchesPrev(league: String?): String {
        return getBasePath() + "eventspastleague.php?id=" + league
    }

    fun getMatchesNext(league: String?): String {
        return getBasePath() + "eventsnextleague.php?id=" + league
    }

    fun getMatchDetail(match: String?): String {
        return getBasePath() + "lookupevent.php?id=" + match
    }

    fun getTeams(league: String?): String {
        return getBasePath() + "search_all_teams.php?l=" + league
    }

    fun getTeamDetail(team: String?): String {
        return getBasePath() + "lookupteam.php?id=" + team
    }

    fun getPlayers(team: String?): String {
        return getBasePath() + "lookup_all_players.php?id=" + team
    }

    fun getPlayerDetail(player: String?): String {
        return getBasePath() + "lookupplayer.php?id=" + player
    }

    fun getMatchesSearch(team: String?): String {
        return getBasePath() + "searchevents.php?e=" + team
    }

    fun getTeamsSearch(team: String?): String {
        return getBasePath() + "searchteams.php?t=" + team
    }

    fun getLeagues(): String {
        return getBasePath() + "search_all_leagues.php?s=Soccer"
    }
}