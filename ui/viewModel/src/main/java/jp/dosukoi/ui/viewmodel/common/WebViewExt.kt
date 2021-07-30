package jp.dosukoi.ui.viewmodel.common

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter

@BindingAdapter("url")
fun WebView.setUrl(url: String?) {
    url ?: return
    this.loadUrl(url)
}

@BindingAdapter("postUrl", "postData")
fun WebView.setPostUrl(url: String?, postData: ByteArray?) {
    url ?: return
    postData ?: return
    this.postUrl(url, postData)
}

@BindingAdapter("callback")
fun WebView.setCallback(callback: WebViewCallback?) {
    callback ?: return
    this.webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            callback.onPageStarted()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            callback.onPageFinished()
        }
    }
}

interface WebViewCallback {
    fun onPageStarted()
    fun onPageFinished()
}