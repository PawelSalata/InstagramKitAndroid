package com.pawelsalata.instagramkit

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.*
import kotlinx.android.synthetic.main.activity_instagram_login.*

/**
 * Created by LETTUCE on 17.12.2017.
 */
internal class InstagramLoginActivity : Activity() {
    val TAG = InstagramLoginActivity::class.java.simpleName

    private val ACCESS_TOKEN = "access_token"
    private val ERROR = "error"
    private val EQUAL = "="

    private lateinit var redirectUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram_login)
        val url: String? = intent.getStringExtra(InstagramManager.EXTRA_AUTH_LOGIN_URL)
        redirectUri = intent.getStringExtra(InstagramManager.EXTRA_AUTH_REDIRECT_URI)
        setupWebView(url)
    }

    fun finish(accessToken: String) {
        val data = Intent()
        data.putExtra(InstagramManager.EXTRA_AUTH_TOKEN, accessToken)
        data.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    fun finishWithError(message: String?) {
        val data = Intent().apply { putExtra(InstagramManager.EXTRA_AUTH_ERROR_MESSAGE, message) }
        setResult(Activity.RESULT_CANCELED, data)
        finish()
    }

    private fun setupWebView(url: String?) {
        clearCache()
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = OAuthWebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCacheEnabled(false)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.loadUrl(url)
    }

    private fun clearCache() {
        webView.clearCache(true)
        webView.clearHistory()
        webView.clearFormData()
    }

    inner class OAuthWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return handleUrlResponse(url!!)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            request?.let {
                return handleUrlResponse(request.url.toString())
            }
            return false
        }

        private fun handleUrlResponse(url: String): Boolean {
            Log.d(TAG, "Redirecting URL " + url)
            if (url.startsWith(redirectUri)) {
                if (url.contains(ACCESS_TOKEN)) {
                    val tokens = url.split(EQUAL)
                    finish(tokens[1])
                } else if (url.contains(ERROR)) {
                    val messages = url.split(EQUAL)
                    finishWithError(messages[messages.size - 1])
                }
                return true
            }
            return false
        }
    }
}