package jp.dosukoi.data.api.common

import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.entity.search.Search
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

    @GET("/search/repositories")
    suspend fun findRepositories(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int? = null
    ): Response<Search>
}
