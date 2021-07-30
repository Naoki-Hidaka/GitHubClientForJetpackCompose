package jp.dosukoi.data.api.common

import jp.dosukoi.data.entity.list.Repository
import jp.dosukoi.data.entity.myPage.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiType {

    @GET("/user")
    suspend fun getUser(): Response<User>

    @GET("/user/repos")
    suspend fun getMyRepositoryList(
        @Query("sort") sort: String = "updated"
    ): Response<List<Repository>>
}