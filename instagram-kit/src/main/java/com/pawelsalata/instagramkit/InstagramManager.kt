package com.pawelsalata.instagramkit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.annotation.NonNull
import android.util.Log
import com.pawelsalata.instagramkit.data.AuthData
import com.pawelsalata.instagramkit.listeners.AuthListener

/**
 * Created by LETTUCE on 17.12.2017.
 */
object InstagramManager {
    private val TAG = InstagramManager::class.java.simpleName

    val INSTAGRAM_KIT_AUTH_REQUEST_CODE = 6353
    internal val EXTRA_AUTH_LOGIN_URL = "com.pawelsalata.instagramkit.EXTRA_AUTH_LOGIN_URL"
    internal val EXTRA_AUTH_TOKEN = "com.pawelsalata.instagramkit.EXTRA_AUTH_TOKEN"
    internal val EXTRA_AUTH_ERROR_MESSAGE = "com.pawelsalata.instagramkit.EXTRA_AUTH_ERROR_MESSAGE"
    internal val EXTRA_AUTH_REDIRECT_URI = "com.pawelsalata.instagramkit.EXTRA_AUTH_REDIRECT_URI"

    private var authListener: AuthListener? = null

    fun authorize(@NonNull activity: Activity, @NonNull authData: AuthData, @NonNull callback: AuthListener) {
        if (authData.clientId.isBlank()) {
            throw IllegalArgumentException("Client ID must not be blank.")
        }
        if (authData.redirectUri.isBlank()) {
            throw IllegalArgumentException("Redirect URI must not be blank.")
        }
        if (activity.isFinishing) {
            Log.e(TAG, "Cannot authorize, activity is finishing.")
            return
        }

        authListener = callback
        val loginUrl = buildLoginUrl(authData)
        val intent = Intent(activity, InstagramLoginActivity::class.java).apply {
            putExtra(EXTRA_AUTH_LOGIN_URL, loginUrl.toString())
            putExtra(EXTRA_AUTH_REDIRECT_URI, authData.redirectUri)
        }
        activity.startActivityForResult(intent, INSTAGRAM_KIT_AUTH_REQUEST_CODE)
    }
    /**
     * This method must be called to process response Instagram API response and
     * get accessToken or errorMessage
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != INSTAGRAM_KIT_AUTH_REQUEST_CODE) {
            Log.e(TAG, "Not a Instagram Kit request")
            return
        }

        when (resultCode) {
            Activity.RESULT_OK -> {
                val token = data?.getStringExtra(EXTRA_AUTH_TOKEN)
                authListener?.onSuccess(token)
            }
            Activity.RESULT_CANCELED -> authListener?.onFailure("User canceled")
            else -> {
                val message = data?.getStringExtra(EXTRA_AUTH_ERROR_MESSAGE)
                authListener?.onFailure(message)
            }
        }
        authListener = null
    }

    private fun buildLoginUrl(authData: AuthData): Uri {
        val uriBuilder = Uri.Builder()
        uriBuilder.scheme("https")
                .authority("api.instagram.com")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", authData.clientId)
                .appendQueryParameter("redirect_uri", authData.redirectUri)
                .appendQueryParameter("response_type", "token")
        return uriBuilder.build()
    }

}