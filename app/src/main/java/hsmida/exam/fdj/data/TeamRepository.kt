package hsmida.exam.fdj.data

import hsmida.exam.fdj.di.IoDispatcher
import hsmida.exam.fdj.model.ApiResult
import hsmida.exam.fdj.model.Team
import hsmida.exam.fdj.data.network.TeamService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface TeamRepository {
    suspend fun searchAllTeams(leagueName: String): Flow<ApiResult<List<Team>>>
}

@Singleton
class TeamRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val service: TeamService,
): TeamRepository {

    override suspend fun searchAllTeams(leagueName: String): Flow<ApiResult<List<Team>>> {
        return flow {
            emit(ApiResult.Loading(true))

            val response = service.searchAllTeams(leagueName)
            if (response.isSuccessful) {
                emit(ApiResult.Success(response.body()?.teams))
            } else {
                val errorMessage = response.errorBody()?.string()
                response.errorBody()?.close()
                emit(ApiResult.Error(errorMessage))
            }
        }.flowOn(ioDispatcher).catch {
            emit(ApiResult.Error(it.localizedMessage))
        }
    }

}