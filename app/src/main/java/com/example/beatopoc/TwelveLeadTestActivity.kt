package com.example.beatopoc

import android.app.ProgressDialog
import `in`.sunfox.healthcare.commons.android.spandan_sdk.OnReportGenerationStateListener
import `in`.sunfox.healthcare.commons.android.spandan_sdk.SpandanSDK
import `in`.sunfox.healthcare.commons.android.spandan_sdk.collection.EcgTest
import `in`.sunfox.healthcare.commons.android.spandan_sdk.collection.EcgTestCallback
import `in`.sunfox.healthcare.commons.android.spandan_sdk.conclusion.EcgReport
import `in`.sunfox.healthcare.commons.android.spandan_sdk.enums.DeviceConnectionState
import `in`.sunfox.healthcare.commons.android.spandan_sdk.enums.EcgPosition
import `in`.sunfox.healthcare.commons.android.spandan_sdk.enums.EcgTestType
import `in`.sunfox.healthcare.java.commons.ecg_processor.conclusions.conclusion.TwelveLeadConclusion
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.beatopoc.databinding.ActivityTwelveLeadTestBinding
import com.google.gson.Gson
//import `in`.sunfox.healthcare.commons.android.sericom.SeriCom
import `in`.sunfox.healthcare.commons.android.spandan_sdk.connection.DeviceInfo
import `in`.sunfox.healthcare.commons.android.spandan_sdk.connection.OnDeviceConnectionStateChangeListener

class TwelveLeadTestActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTwelveLeadTestBinding

    private lateinit var spandanSDK: SpandanSDK
    private var ecgPoints : HashMap<EcgPosition,ArrayList<Double>> = hashMapOf()
    private var ecgReport : EcgReport? = null
    private lateinit var ecgTest:EcgTest
    private lateinit var ecgPosition: EcgPosition
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_twelve_lead_test)

        progressDialog = ProgressDialog(this)

        try{
            binding.activityMainTextviewCurrentPosition.text = "Please select the lead"

            spandanSDK = SpandanSDK.getInstance()

            /**
             * step :-2
             * set callback for device connectivity.*/
            spandanSDK.setOnDeviceConnectionStateChangedListener(object : OnDeviceConnectionStateChangeListener{
                override fun onDeviceConnectionStateChanged(deviceConnectionState: DeviceConnectionState) {
                    when(deviceConnectionState){
                        DeviceConnectionState.DISCONNECTED -> { binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(
                            Color.RED) }
                        DeviceConnectionState.CONNECTED -> {  }
                        DeviceConnectionState.VERIFICATION_TIME_OUT -> {}
                        DeviceConnectionState.USB_CONNECTION_PERMISSION_DENIED -> {}
                    }
                }

                override fun onDeviceTypeChange(deviceType: String) {

                }

                override fun onDeviceVerified(deviceInfo: DeviceInfo) {

                }

            })
//            spandanSDK.setOnDeviceConnectionStateChangedListener {
//                when(it){
//                    DeviceConnectionState.DISCONNECTED -> { binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(
//                        Color.RED) }
//                    DeviceConnectionState.CONNECTED -> {  }
//                    DeviceConnectionState.VERIFIED -> { binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(
//                        Color.GREEN) }
//                    DeviceConnectionState.VERIFICATION_TIME_OUT -> {}
//                    DeviceConnectionState.USB_CONNECTION_PERMISSION_DENIED -> {}
//                }
//            }
            /**
             * step :-3
             * create ecg test..*/
            ecgTest = spandanSDK.createTest(EcgTestType.TWELVE_LEAD,object : EcgTestCallback{
                override fun onTestFailed(statusCode: Int) {

                }

                override fun onTestStarted(ecgPosition: EcgPosition) {

                }

                override fun onElapsedTimeChanged(elapsedTime: Long, remainingTime: Long) {
                    binding.activityMainProgressbarTestStatus.progress = elapsedTime.toInt()
                    when(ecgPosition){
                        EcgPosition.V1 -> {
                            binding.progressBar.progress = elapsedTime.toInt()
                        }
                        EcgPosition.V2 -> {
                            binding.progressBar2.progress = elapsedTime.toInt()
                        }
                        EcgPosition.V3 -> {
                            binding.progressBar3.progress = elapsedTime.toInt()
                        }
                        EcgPosition.V4 -> {
                            binding.progressBar4.progress = elapsedTime.toInt()
                        }
                        EcgPosition.V5 -> {
                            binding.progressBar5.progress = elapsedTime.toInt()
                        }
                        EcgPosition.V6 -> {
                            binding.progressBar6.progress = elapsedTime.toInt()
                        }
                        EcgPosition.LEAD_2 -> {
                            binding.progressBar8.progress = elapsedTime.toInt()
                        }
                        EcgPosition.LEAD_1 -> {
                            binding.progressBar7.progress = elapsedTime.toInt()
                        }
                    }
                }

                override fun onReceivedData(data: String) {

                }

                override fun onPositionRecordingComplete(
                    ecgPosition: EcgPosition,
                    ecgPoints: ArrayList<Double>?
                ) {
                    runOnUiThread {
                        Toast.makeText(this@TwelveLeadTestActivity,"lead completed",Toast.LENGTH_SHORT).show()
                    }
                    if(ecgPoints!=null)
                        this@TwelveLeadTestActivity.ecgPoints[ecgPosition] = ecgPoints
                }

            },(application as BeatoApplication).token!!)

            binding.activityMainLayoutDeviceConnectionStatus.setBackgroundColor(if(SpandanSDK.getInstance().isDeviceConnected()) Color.GREEN else Color.RED)

            binding.progressBar.setOnClickListener {
                ecgPosition = EcgPosition.V1
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.V1.name
            }

            binding.progressBar2.setOnClickListener {
                ecgPosition = EcgPosition.V2
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.V2.name
            }


            binding.progressBar3.setOnClickListener {
                ecgPosition = EcgPosition.V3
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.V3.name
            }
            binding.progressBar4.setOnClickListener {
                ecgPosition = EcgPosition.V4
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.V4.name
            }
            binding.progressBar5.setOnClickListener {
                ecgPosition = EcgPosition.V5
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.V5.name
            }
            binding.progressBar6.setOnClickListener {
                ecgPosition = EcgPosition.V6
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.V6.name
            }
            binding.progressBar7.setOnClickListener {
                ecgPosition = EcgPosition.LEAD_1
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.LEAD_1.name
            }
            binding.progressBar8.setOnClickListener {
                ecgPosition = EcgPosition.LEAD_2
                binding.activityMainTextviewCurrentPosition.text = EcgPosition.LEAD_2.name
            }

            /***
             * step :-4
             * start ecg test.*/
            binding.activityMainBtnStartTest.setOnClickListener {
                if(!SpandanSDK.getInstance().isDeviceConnected())
                    Toast.makeText(this,"Please connect the device first.", Toast.LENGTH_SHORT).show()
                else if(!::ecgPosition.isInitialized)
                    Toast.makeText(this@TwelveLeadTestActivity,"please select any lead", Toast.LENGTH_SHORT).show()
                else
                    ecgTest.start(ecgPosition)
            }


            /**
             * step :-5
             * generate ecg report*/
            binding.activityMainBtnGenerateReport.setOnClickListener {
                if(ecgPoints.size > 0){
                    showProgressDialog()
                    spandanSDK.generateReport(12,ecgPoints,(application as BeatoApplication).token!!,object : OnReportGenerationStateListener{
                        override fun onReportGenerationSuccess(p0: EcgReport) {
                            ecgReport = p0
                            runOnUiThread {
                                hideProgressDialog()
    //                            binding.reportConclusion.movementMethod = ScrollingMovementMethod()
    //                            binding.reportConclusion.text = "Json Format : \n${Gson().toJson(p0.ecgData)}"
                                Toast.makeText(this@TwelveLeadTestActivity,"report generated",Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onReportGenerationFailed(p0: Int, p1: String) {
                            runOnUiThread {
                                hideProgressDialog()
                                Toast.makeText(this@TwelveLeadTestActivity,p1,Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
                }
                else{
                    Toast.makeText(this@TwelveLeadTestActivity,"Please complete the test",Toast.LENGTH_SHORT).show()
                }
            }

            binding.activityMainBtnShowConclusion.setOnClickListener {
                ecgReport?.let {
                    val conclusion = (ecgReport?.conclusion as TwelveLeadConclusion)
                    val characteristics = ecgReport?.ecgCharacteristics
                    binding.reportConclusion.text = "$conclusion $characteristics"
                }
            }
        }
        catch (e:Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
        }

    }

    private fun showProgressDialog() {
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Generating report....")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    private fun hideProgressDialog(){
        progressDialog.dismiss()
    }
}