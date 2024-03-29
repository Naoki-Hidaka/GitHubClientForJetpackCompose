package jp.dosukoi.githubclientforjetpackcompose

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.dosukoi.data.api.common.AccessTokenProvider
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.api.common.IAuthApiType
import jp.dosukoi.data.repository.common.AppDatabase
import jp.dosukoi.githubclient.domain.entity.auth.AuthDao
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    @Named("retrofit")
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val format = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(okHttpClient)
            .addConverterFactory(format.asConverterFactory(contentType))
            .build()
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val format = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://github.com")
            .client(okHttpClient)
            .addConverterFactory(format.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideIApiType(
        @Named("retrofit") retrofit: Retrofit
    ): IApiType {
        return retrofit.create(IApiType::class.java)
    }

    @Provides
    @Singleton
    fun provideIAuthApiType(
        @Named("authRetrofit") retrofit: Retrofit
    ): IAuthApiType {
        return retrofit.create(IAuthApiType::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        accessTokenProvider: AccessTokenProvider
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor { chain ->
                val token = accessTokenProvider.provide()
                val request = chain.request().newBuilder()
                    .header("Authorization", "token $token")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Named("clientId")
    @Provides
    @Singleton
    fun provideClientId(): String {
        return "52b65f6025ea1e4264cd"
    }

    @Named("clientSecret")
    @Provides
    @Singleton
    fun provideClientSecret(): String {
        return "ac066b7d2508febfcb335faf63928379cb4dcda0"
    }

    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    @Provides
    @Singleton
    fun provideAuthDao(
        appDatabase: AppDatabase
    ): AuthDao {
        return appDatabase.authDao()
    }
}
