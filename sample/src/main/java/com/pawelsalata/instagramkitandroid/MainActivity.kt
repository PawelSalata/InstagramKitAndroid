package com.pawelsalata.instagramkitandroid

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.pawelsalata.instagramkit.InstagramManager
import com.pawelsalata.instagramkit.data.AuthData
import com.pawelsalata.instagramkit.listeners.AuthListener
import com.pawelsalata.instagramkitandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainListener {
    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.listener = this
    }

    override fun onInstagramClicked(v: View) {
        InstagramManager.authorize(
                this,
                AuthData(getString(R.string.instagram_app_id), getString(R.string.redirect_uri)),
                object : AuthListener {
                    override fun onSuccess(accessToken: String?) {
                        Log.d(TAG, "Access token: $accessToken")
                    }

                    override fun onFailure(errorMessage: String?) {
                        Log.d(TAG, "Error: $errorMessage")
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == InstagramManager.INSTAGRAM_KIT_AUTH_REQUEST_CODE) {
            InstagramManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}
