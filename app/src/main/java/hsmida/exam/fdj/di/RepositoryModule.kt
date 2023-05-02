package hsmida.exam.fdj.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hsmida.exam.fdj.data.LeagueRepository
import hsmida.exam.fdj.data.LeagueRepositoryImpl
import hsmida.exam.fdj.data.TeamRepository
import hsmida.exam.fdj.data.TeamRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTeamRepository(impl: TeamRepositoryImpl): TeamRepository

    @Binds
    abstract fun bindLeagueRepository(impl: LeagueRepositoryImpl): LeagueRepository
}