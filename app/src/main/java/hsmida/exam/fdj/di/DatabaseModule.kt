package hsmida.exam.fdj.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hsmida.exam.fdj.data.database.AppDatabase
import hsmida.exam.fdj.data.database.LeagueDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "application.db"
        ).build()
    }

    @Provides
    fun provideLeagueDao(appDatabase: AppDatabase): LeagueDao {
        return appDatabase.leagueDao()
    }
}