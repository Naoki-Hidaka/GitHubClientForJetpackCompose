package jp.dosukoi.ui.view.common

import android.content.Context
import coil.ImageLoader
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompositionLocalProvider @Inject constructor(
    private val context: Context,
    private val okHttpClient: OkHttpClient
) {

    fun provideImageLoader() = ImageLoader.Builder(context).okHttpClient(okHttpClient).build()
}
