package com.example.beatopoc

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


class RetrofitHelper {

    fun getRetrofitInstance():TokenRefreshApi{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.sunfox.in")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(TokenRefreshApi::class.java)
    }
}
class TokenRefreshResult(
    @SerializedName("message")
    var message: String,
    @SerializedName("token")
    var token: String,
)
interface TokenRefreshApi{
    @Headers("Authorization: 4u838u43u439u3","Used-Token: y7y6y5")
    @GET("/v2/spandan/token-refresh/")
    fun getToken():Call<TokenRefreshResult>
}