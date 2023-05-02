package hsmida.exam.fdj.data.network

import hsmida.exam.fdj.model.AllLeaguesResponse
import retrofit2.Response
import retrofit2.http.GET

interface LeagueService {

    @GET("/api/{api_version}/json/{api_key}/all_leagues.php")
    suspend fun getAllLeagues() : Response<AllLeaguesResponse>

}