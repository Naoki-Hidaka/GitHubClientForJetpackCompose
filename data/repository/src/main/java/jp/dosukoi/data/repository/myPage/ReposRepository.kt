package jp.dosukoi.data.repository.myPage

import dagger.Reusable
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.repository.common.asyncFetch
import javax.inject.Inject

@Reusable
class ReposRepository @Inject constructor(
    private val api: IApiType
) {

    suspend fun getRepositoryList(): List<Repository> {
        return asyncFetch { api.getMyRepositoryList() }
    }
}