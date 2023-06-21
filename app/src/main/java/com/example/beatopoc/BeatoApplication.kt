package com.example.beatopoc


import `in`.sunfox.healthcare.commons.android.spandan_sdk.OnInitializationCompleteListener
import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import android.app.Application

class BeatoApplication:Application() {

    var token : String?=null

    /**
     * step :-1
     * sdk initialization*/
    override fun onCreate() {
        super.onCreate()
        SpandanSDK.initialize(this@BeatoApplication,"enter your api key",object : OnInitializationCompleteListener{
            override fun onInitializationSuccess(authenticationToken: String) {
                token = authenticationToken
            }

            override fun onInitializationFailed(message: String) {

            }

        })
    }
}