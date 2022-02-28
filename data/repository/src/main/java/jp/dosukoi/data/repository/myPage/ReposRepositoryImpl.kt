package jp.dosukoi.data.repository.myPage

import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.repository.common.asyncFetch
import jp.dosukoi.githubclient.domain.entity.auth.UnAuthorizeException
import jp.dosukoi.githubclient.domain.entity.myPage.Repository
import jp.dosukoi.githubclient.domain.repository.myPage.ReposRepository
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReposRepositoryImpl @Inject constructor(
    private val api: IApiType
) : ReposRepository {

    override suspend fun getRepositoryList(): List<Repository> {
        return try {
            asyncFetch { api.getMyRepositoryList() }
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                when (throwable.code()) {
                    401, 403 -> throw UnAuthorizeException(throwable.message())
                    else -> throw throwable
                }
            } else {
                throw throwable
            }
        }
    }
}
