package hsmida.exam.fdj.data.network

import hsmida.exam.fdj.model.SearchAllTeamsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TeamService {

    @GET("/api/{api_version}/json/{api_key}/search_all_teams.php")
    suspend fun searchAllTeams(@Query("l") leagueName: String): Response<SearchAllTeamsResponse>

}