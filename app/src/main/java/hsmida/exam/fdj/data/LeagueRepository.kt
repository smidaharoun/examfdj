package hsmida.exam.fdj.data

import hsmida.exam.fdj.di.IoDispatcher
import hsmida.exam.fdj.model.ApiResult
import hsmida.exam.fdj.model.League
import hsmida.exam.fdj.data.database.LeagueDao
import hsmida.exam.fdj.data.network.LeagueService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface LeagueRepository {
    suspend fun getAllLeagues(): Flow<ApiResult<List<League>>>
}

class LeagueRepositoryImpl @Inject constructor(
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val service: LeagueService,
    private val dao: LeagueDao,
): LeagueRepository {

    override suspend fun getAllLeagues(): Flow<ApiResult<List<League>>> {
        return flow {
            val result = dao.getAll()
            if (result.isNotEmpty()) {
                emit(ApiResult.Success(dao.getAll()))
                return@flow
            }

            emit(ApiResult.Loading(true))

            val response = service.getAllLeagues()
            if (response.isSuccessful) {
                response.body()?.leagues?.let {
                    dao.insertAll(it)
                }
                emit(ApiResult.Success(response.body()?.leagues))
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

