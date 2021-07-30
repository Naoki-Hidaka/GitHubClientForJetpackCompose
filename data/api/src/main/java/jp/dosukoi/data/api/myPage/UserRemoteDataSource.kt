package jp.dosukoi.data.api.myPage

import dagger.Reusable
import jp.dosukoi.data.entity.myPage.User
import retrofit2.Response
import javax.inject.Inject

@Reusable
class UserRemoteDataSource @Inject constructor() {

    suspend fun getUser(): Response<User> {
        return Response.success(
            User(
                "hoge",
                1,
                "https://via.placeholder.com/150",
                "https://api.github.com/users/Naoki-Hidaka",
                "https://github.com/Naoki-Hidaka",
                "Kyash Inc.",
                "naoki6855@gmail.com"
            )
        )
    }
}