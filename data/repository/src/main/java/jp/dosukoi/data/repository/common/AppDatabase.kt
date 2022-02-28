package jp.dosukoi.data.repository.common

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.dosukoi.githubclient.domain.entity.auth.AuthDao
import jp.dosukoi.githubclient.domain.entity.auth.AuthEntity
import javax.inject.Singleton

@Singleton
@Database(entities = [AuthEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
}
