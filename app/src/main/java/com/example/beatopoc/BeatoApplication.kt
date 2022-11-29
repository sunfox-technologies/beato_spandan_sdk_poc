package com.example.beatopoc

import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import android.app.Application
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeatoApplication:Application() {

    /**
     * step :-1
     * sdk initialization*/
    override fun onCreate() {
        super.onCreate()
        try{
            SpandanSDK.initialize("-AcToLlJOY2Xyqui1AIumMlwEt5KsbzJIvR5krINljsGZkyfK3ZyFkllcB33xcuD-fiUNa5ypoD7ANeSbyDtWFkAZIoKWLhqWRYOZdsjyEfI1PcurVqQuPSeiNCujw8-_wKI6KoRcA0naBq3Rqq3bRAwF2UjMNVRQxXLQKVUl82WClTRKeDp_RJrfLPGu_QCRy7bfLJWejk0kHm5Y9YvLSlJQ5Gr9r3DXKxYGrd9ud5oRnZepeXWWg0y8EFxjwPvjgffYFPQ2RDv8QD9sfgpuRZ_3HcfCvpqonqk-8CYe3yAHy9cKMZLxVjm7OKcJ_rWQfuHMCkB0xh68g5QiuYvGg",this)
        }catch (e:Exception){
            Thread{
                RetrofitHelper().
                getRetrofitInstance().
                getToken().
                enqueue(object : Callback<TokenRefreshResult>{
                    override fun onResponse(
                        call: Call<TokenRefreshResult>,
                        response: Response<TokenRefreshResult>,
                    ) {
                        response.body()?.let {
                            SpandanSDK.initialize(it.token,this@BeatoApplication)
                        }
                    }
                    override fun onFailure(call: Call<TokenRefreshResult>, t: Throwable) {

                    }

                })
            }.start()

        }
    }
}