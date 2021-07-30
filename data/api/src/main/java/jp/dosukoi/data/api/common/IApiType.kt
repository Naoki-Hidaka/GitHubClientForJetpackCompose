package jp.dosukoi.data.api.common

import jp.dosukoi.data.entity.auth.Auth
import jp.dosukoi.data.entity.myPage.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IApiType {
    @POST("/login/oauth/access_token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String
    ): Response<Auth>

    @GET("/user")
    suspend fun getUser(): Response<User>
}