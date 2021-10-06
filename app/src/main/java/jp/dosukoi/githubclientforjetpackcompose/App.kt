package jp.dosukoi.githubclientforjetpackcompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var importantDebugLogReportingTree: ImportantDebugLogReportingTree

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree(), importantDebugLogReportingTree)
        Timber.d("debug: onCreate")
    }
}
