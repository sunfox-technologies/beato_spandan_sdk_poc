package com.example.beatopoc

import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import android.app.Application

class BeatoApplication:Application() {

    /**
     * step :-1
     * sdk initialization*/
    override fun onCreate() {
        super.onCreate()
        SpandanSDK.initialize("-AcToLlJOY2Xyqui1AIumMlwEt5KsbzJIvR5krINljsGZkyfK3ZyFkllcB33xcuDknzhPfVZpffOKE2-EOoBylkAZIoKWLhqWRYOZdsjyEdrawAz_iUxoWABps_52D6VbvBs5SCl78asap8yD1gLZBAwF2UjMNVRQxXLQKVUl82WClTRKeDp_RJrfLPGu_QCRy7bfLJWejk0kHm5Y9YvLSlJQ5Gr9r3DXKxYGrd9ud76eth01ZhL8Y_NugOtJQ2l7qBQy3sdOSOoiTs-uxpDprJ0hI8vozfCp_hsLB3Uhf3jftvFY6-aUGlCG8_Y0352r7OGdKHFKlxmXEGGn4cvvQ",this)
    }
}