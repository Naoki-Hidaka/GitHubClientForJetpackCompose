package jp.dosukoi.data.repository.top

import dagger.Reusable
import javax.inject.Inject

@Reusable
class AppRepository @Inject constructor() {
    suspend fun isFirstOpenApp(): Boolean {
        return true
    }
}