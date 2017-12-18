package com.pawelsalata.instagramkit.listeners

/**
 * Created by LETTUCE on 17.12.2017.
 */
interface AuthListener {
    fun onSuccess(accessToken: String?)
    fun onFailure(errorMessage: String?)
}