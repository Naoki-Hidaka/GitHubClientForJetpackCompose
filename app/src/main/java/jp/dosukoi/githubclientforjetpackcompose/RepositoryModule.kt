package jp.dosukoi.githubclientforjetpackcompose

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.api.common.IAuthApiType
import jp.dosukoi.data.repository.auth.AuthRepositoryImpl
import jp.dosukoi.data.repository.myPage.ReposRepositoryImpl
import jp.dosukoi.data.repository.myPage.UserRepositoryImpl
import jp.dosukoi.data.repository.search.SearchRepositoryImpl
import jp.dosukoi.githubclient.domain.entity.auth.AuthDao
import jp.dosukoi.githubclient.domain.repository.auth.AuthRepository
import jp.dosukoi.githubclient.domain.repository.myPage.ReposRepository
import jp.dosukoi.githubclient.domain.repository.myPage.UserRepository
import jp.dosukoi.githubclient.domain.repository.search.SearchRepository
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(
        @Named("clientId") clientId: String,
        @Named("clientSecret") clientSecret: String,
        api: IAuthApiType,
        authDao: AuthDao
    ): AuthRepository = AuthRepositoryImpl(clientId, clientSecret, api, authDao)

    @Singleton
    @Provides
    fun provideReposRepository(api: IApiType): ReposRepository = ReposRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideUserRepository(api: IApiType): UserRepository = UserRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideSearchRepository(api: IApiType): SearchRepository = SearchRepositoryImpl(api)
}
