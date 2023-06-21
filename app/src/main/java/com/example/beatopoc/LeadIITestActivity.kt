package com.example.beatopoc

import `in`.sunfox.healthcare.commons.android.spandan_sdk.OnReportGenerationStateListener
import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import `in`.sunfox.healthcare.commons.android.spandan_sdk.collection.EcgTest
import `in`.sunfox.healthcare.commons.android.spandan_sdk.collection.EcgTestCallback
import `in`.sunfox.healthcare.commons.android.spandan_sdk.conclusion.EcgReport
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
import com.example.beatopoc.databinding.ActivityLeadIitestBinding
import `in`.sunfox.healthcare.commons.android.sericom.SeriCom
import `in`.sunfox.healthcare.commons.android.spandan_sdk.connection.OnDeviceConnectionStateChangeListener

class LeadIITestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeadIitestBinding
    private lateinit var spandanSDK: SpandanSDK
    private var ecgPoints : HashMap<EcgPosition,ArrayList<Double>> = hashMapOf()
    private var ecgReport : EcgReport? = null
    private lateinit var ecgTest: EcgTest
    private lateinit var ecgPosition: EcgPosition
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lead_iitest)

        binding.activityMainTextviewCurrentPosition.text = "Please select the lead"

        try {
            spandanSDK = SpandanSDK.getInstance()

            /**
             * step :-2
             * set callback for device connectivity.*/
            spandanSDK.setOnDeviceConnectionStateChangedListener(object : OnDeviceConnectionStateChangeListener{
                override fun onDeviceConnectionStateChanged(deviceConnectionState: DeviceConnectionState) {
                    when (deviceConnectionState) {
                        DeviceConnectionState.DISCONNECTED -> {
                            binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(Color.RED)
                        }
                        DeviceConnectionState.CONNECTED -> {}
//                        DeviceConnectionState.VERIFIED -> {
//                            binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(Color.GREEN)
//                        }
                        DeviceConnectionState.VERIFICATION_TIME_OUT -> {}
                        DeviceConnectionState.USB_CONNECTION_PERMISSION_DENIED -> {}
                    }
                }

                override fun onDeviceTypeChange(deviceType: String) {

                }

                override fun onDeviceVerified() {

                }

            })
//            spandanSDK.setOnDeviceConnectionStateChangedListener {
//                when (it) {
//                    DeviceConnectionState.DISCONNECTED -> {
//                        binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(Color.RED)
//                    }
//                    DeviceConnectionState.CONNECTED -> {}
//                    DeviceConnectionState.VERIFIED -> {
//                        binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(Color.GREEN)
//                    }
//                    DeviceConnectionState.VERIFICATION_TIME_OUT -> {}
//                    DeviceConnectionState.USB_CONNECTION_PERMISSION_DENIED -> {}
//                }
//            }
            /**
             * step :-3
             * create ecg test..*/
            ecgTest = spandanSDK.createTest(EcgTestType.LEAD_TWO, object : EcgTestCallback {
                override fun onTestFailed(statusCode: Int) {

                }

                override fun onTestStarted(ecgPosition: EcgPosition) {
                    Toast.makeText(this@LeadIITestActivity, "test started", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onElapsedTimeChanged(elapsedTime: Long, remainingTime: Long) {
                    binding.activityMainProgressbarTestStatus.progress = elapsedTime.toInt()
                    binding.progressBar8.progress = elapsedTime.toInt()
                }

                override fun onReceivedData(data: String) {

                }

                override fun onPositionRecordingComplete(
                    ecgPosition: EcgPosition,
                    ecgPoints: ArrayList<Double>?
                ) {
                    if (ecgPoints!=null){
                        this@LeadIITestActivity.ecgPoints[ecgPosition] = ecgPoints
                    }
                }

            }, (application as BeatoApplication).token!!)


            binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(if (SeriCom.isDeviceConnected()) Color.GREEN else Color.RED)


            binding.progressBar8.setOnClickListener {
                ecgPosition = EcgPosition.LEAD_2
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.LEAD_2.name
            }

            /***
             * step :-4
             * start ecg test.*/
            binding.activityMainBtnStartTest.setOnClickListener {
                if (!SeriCom.isDeviceConnected())
                    Toast.makeText(this, "Please connect the device first.", Toast.LENGTH_SHORT)
                        .show()
                else if (!::ecgPosition.isInitialized)
                    Toast.makeText(this, "please select any lead", Toast.LENGTH_SHORT).show()
                else
                    ecgTest.start(ecgPosition)
            }


            /**
             * step :-5
             * generate ecg report*/
            binding.activityMainBtnGenerateReport.setOnClickListener {
                spandanSDK.generateReport(12,
                    ecgPoints,
                    (application as BeatoApplication).token!!,
                    object : OnReportGenerationStateListener {
                        override fun onReportGenerationSuccess(ecgReport: EcgReport) {
                            this@LeadIITestActivity.ecgReport = ecgReport
                            runOnUiThread {
                                Toast.makeText(this@LeadIITestActivity,
                                    "report generated",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onReportGenerationFailed(errorCode: Int, errorMsg: String) {
                            Log.d("SdkImpl.TAG", "onReportGenerationFailed: $errorMsg")
                            runOnUiThread {
                                Toast.makeText(this@LeadIITestActivity, errorMsg, Toast.LENGTH_SHORT)
                                    .show()
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
        } catch (e: Exception) {
            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
        }
    }
}