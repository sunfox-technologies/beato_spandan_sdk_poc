package com.example.beetopoc

import `in`.sunfox.healthcare.commons.android.spandan_sdk.OnReportGenerationStateListener
import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import `in`.sunfox.healthcare.commons.android.spandan_sdk.collection.EcgTest
import `in`.sunfox.healthcare.commons.android.spandan_sdk.collection.EcgTestCallback
import `in`.sunfox.healthcare.commons.android.spandan_sdk.conclusion.Conclusion
import `in`.sunfox.healthcare.commons.android.spandan_sdk.conclusion.EcgReport
import `in`.sunfox.healthcare.commons.android.spandan_sdk.connection.usb_connection.UsbConnectionHelper
import `in`.sunfox.healthcare.commons.android.spandan_sdk.enums.DeviceConnectionState
import `in`.sunfox.healthcare.commons.android.spandan_sdk.enums.EcgPosition
import `in`.sunfox.healthcare.commons.android.spandan_sdk.enums.EcgTestType
import `in`.sunfox.healthcare.java.commons.ecg_processor.conclusions.conclusion.LeadTwoConclusion
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.beetopoc.databinding.ActivityLeadIitestBinding

class LeadIITestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeadIitestBinding
    private lateinit var spandanSDK: SpandanSDK
    private var ecgPoints : HashMap<EcgPosition,ArrayList<Double>> = hashMapOf()
    private var ecgReport : EcgReport? = null
    private lateinit var ecgTest: EcgTest
    private lateinit var ecgPosition: EcgPosition
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_lead_iitest)

        binding.activityMainTextviewCurrentPosition.text = "Please select the lead"

        /**
         * step :-1
         * sdk initialization*/
        SpandanSDK.initialize("-AcToLlJOY2Xyqui1AIumMlwEt5KsbzJIvR5krINljsGZkyfK3ZyFkllcB33xcuDknzhPfVZpffOKE2-EOoBylkAZIoKWLhqWRYOZdsjyEdrawAz_iUxoWABps_52D6VbvBs5SCl78asap8yD1gLZBAwF2UjMNVRQxXLQKVUl82WClTRKeDp_RJrfLPGu_QCRy7bfLJWejk0kHm5Y9YvLSlJQ5Gr9r3DXKxYGrd9ud76eth01ZhL8Y_NugOtJQ2l7qBQy3sdOSOoiTs-uxpDprJ0hI8vozfCp_hsLB3Uhf3jftvFY6-aUGlCG8_Y0352r7OGdKHFKlxmXEGGn4cvvQ",application)
        spandanSDK = SpandanSDK.getInstance()

        /**
         * step :-2
         * set callback for device connectivity.*/
        spandanSDK.setOnDeviceConnectionStateChangedListener {
            when(it){
                DeviceConnectionState.DISCONNECTED -> { binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(Color.RED) }
                DeviceConnectionState.CONNECTED -> {  }
                DeviceConnectionState.VERIFIED -> { binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(Color.GREEN) }
                DeviceConnectionState.VERIFICATION_TIME_OUT -> {}
                DeviceConnectionState.USB_CONNECTION_PERMISSION_DENIED -> {}
            }
        }
        /**
         * step :-3
         * create ecg test..*/
        ecgTest = spandanSDK.createTest(EcgTestType.LEAD_TWO,object : EcgTestCallback {
            override fun onTestFailed(p0: Int) {

            }

            override fun onTestStarted(p0: EcgPosition) {
                Toast.makeText(this@LeadIITestActivity,"test started",Toast.LENGTH_SHORT).show()
            }

            override fun onElapsedTimeChanged(p0: Long, p1: Long) {
                binding.activityMainProgressbarTestStatus.progress = p0.toInt()
                binding.progressBar8.progress = p0.toInt()
            }

            override fun onReceivedData(p0: String) {

            }

            override fun onPositionRecordingComplete(
                p0: EcgPosition,
                p1: java.util.ArrayList<Double>?,
            ) {
                if(p1!=null)
                    ecgPoints[p0] = p1
            }

        })


        binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(if(UsbConnectionHelper.INSTANCE.isDeviceConnected) Color.GREEN else Color.RED)


        binding.progressBar8.setOnClickListener {
            ecgPosition = EcgPosition.LEAD_2
            binding.activityMainTextviewCurrentPosition.text = EcgPosition.LEAD_2.name
        }

        /***
         * step :-4
         * start ecg test.*/
        binding.activityMainBtnStartTest.setOnClickListener {
            if(!::ecgPosition.isInitialized)
                Toast.makeText(this,"please select any lead", Toast.LENGTH_SHORT).show()
            else if(!UsbConnectionHelper.INSTANCE.isDeviceConnected)
                Toast.makeText(this,"Please connect the device first.", Toast.LENGTH_SHORT).show()
            else
                ecgTest.start(ecgPosition)
        }



        /**
         * step :-5
         * generate ecg report*/
        binding.activityMainBtnGenerateReport.setOnClickListener {
            spandanSDK.generateReport(12,ecgPoints,object : OnReportGenerationStateListener {
                override fun onReportGenerationSuccess(p0: EcgReport) {
                    ecgReport = p0
                    runOnUiThread {
                        Toast.makeText(this@LeadIITestActivity,"report generated",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onReportGenerationFailed(p0: Int, p1: String) {
                    Log.d("SdkImpl.TAG", "onReportGenerationFailed: $p1")
                    runOnUiThread {
                        Toast.makeText(this@LeadIITestActivity,"$p1",Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }

        binding.activityMainBtnShowConclusion.setOnClickListener {
            ecgReport?.let {
                val conclusion = (ecgReport?.conclusion as LeadTwoConclusion)
                val characteristics = ecgReport?.ecgCharacteristics
                binding.result.text = "$conclusion $characteristics"
            }
        }

    }
}