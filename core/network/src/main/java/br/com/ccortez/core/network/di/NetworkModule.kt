package br.com.ccortez.core.network.di

import br.com.ccortez.core.network.ApiService
import br.com.ccortez.core.network.dataproviders.RideHistoryDataProviders
import br.com.ccortez.core.network.dataproviders.TaxiTravelOptionsDataProviders
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val TIMEOUT = 30L

    @Provides
    fun provideApiService(
        client: OkHttpClient,
        gson: GsonConverterFactory,
    ): ApiService {
        val baseUrl = br.com.ccortez.core.network.BuildConfig.API_BASE_URL

        return Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(gson)
            .build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttp(_logger: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
    }.build()

    @Provides
    @Singleton
    internal fun provideInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    internal fun provideGsonClient(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideTaxiTravelOptionsDataProvider(apiService: ApiService): TaxiTravelOptionsDataProviders {
        return TaxiTravelOptionsDataProviders(apiService)
    }

    @Provides
    fun provideRideHistoryDataProviders(apiService: ApiService): RideHistoryDataProviders {
        return RideHistoryDataProviders(apiService)
    }

}