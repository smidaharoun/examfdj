package hsmida.exam.fdj.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hsmida.exam.fdj.BuildConfig
import hsmida.exam.fdj.data.network.AuthenticationInterceptor
import hsmida.exam.fdj.data.network.LeagueService
import hsmida.exam.fdj.data.network.TeamService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor;
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthenticationInterceptor(BuildConfig.API_KEY, BuildConfig.API_VERSION))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideLeagueService(retrofit: Retrofit): LeagueService {
        return retrofit.create(LeagueService::class.java)
    }

    @Singleton
    @Provides
    fun provideTeamService(retrofit: Retrofit): TeamService {
        return retrofit.create(TeamService::class.java)
    }

}