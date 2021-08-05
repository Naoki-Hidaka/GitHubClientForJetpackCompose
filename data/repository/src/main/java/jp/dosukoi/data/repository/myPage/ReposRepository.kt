package jp.dosukoi.data.repository.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.Reusable
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.repository.common.asyncFetch
import javax.inject.Inject

@Reusable
class ReposRepository @Inject constructor(
    private val api: IApiType
) {

    private val _repositoryList = MutableLiveData<List<Repository>>()
    val repositoryList: LiveData<List<Repository>> = _repositoryList

    suspend fun getRepositoryList() {
        _repositoryList.value = asyncFetch { api.getMyRepositoryList() }
    }
}