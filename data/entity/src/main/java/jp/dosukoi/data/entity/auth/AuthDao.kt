package jp.dosukoi.data.entity.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(auth: AuthEntity)

    @Query("SELECT * FROM AuthEntity WHERE id = 0")
    fun getAuth(): Auth?
}
