package com.example.beatopoc


import `in`.sunfox.healthcare.commons.android.spandan_sdk.OnInitializationCompleteListener
import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import android.app.Application
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeatoApplication:Application() {

    lateinit var token : String

    /**
     * step :-1
     * sdk initialization*/
    override fun onCreate() {
        super.onCreate()
        SpandanSDK.initialize(this@BeatoApplication,"4u838u43u439u3",object : OnInitializationCompleteListener{
            override fun onInitializationSuccess(p0: String) {
                token = p0
            }

            override fun onInitializationFailed(p0: String) {

            }

        })
//        try{
//            SpandanSDK.initialize(this,"4u838u43u439u3",object : OnInitializationCompleteListener{
//                override fun onInitializationSuccess(p0: String) {
//                    token = p0
//                }
//
//                override fun onInitializationFailed(p0: String) {
//
//                }
//
//            })
//        }catch (e:Exception){
//            Thread{
//                RetrofitHelper().
//                getRetrofitInstance().
//                getToken().
//                enqueue(object : Callback<TokenRefreshResult>{
//                    override fun onResponse(
//                        call: Call<TokenRefreshResult>,
//                        response: Response<TokenRefreshResult>,
//                    ) {
//                        response.body()?.let {
//                            SpandanSDK.initialize(it.token,this@BeatoApplication)
//                        }
//                    }
//                    override fun onFailure(call: Call<TokenRefreshResult>, t: Throwable) {
//
//                    }
//
//                })
//            }.start()
//        }
    }
}