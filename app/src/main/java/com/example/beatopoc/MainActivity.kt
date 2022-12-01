package com.example.beatopoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.beatopoc.LeadIITestActivity
import com.example.beatopoc.R
import com.example.beatopoc.TwelveLeadTestActivity
import com.example.beatopoc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.twelveLeadTest.setOnClickListener {
            if ((application as BeatoApplication).token == null) {
                Toast.makeText(this, "token not initialized", Toast.LENGTH_SHORT).show()
            } else
                startActivity(Intent(this, TwelveLeadTestActivity::class.java))
        }

        binding.leadIITest.setOnClickListener {
            if ((application as BeatoApplication).token == null) {
                Toast.makeText(this, "token not initialized", Toast.LENGTH_SHORT).show()
            } else
                startActivity(Intent(this, LeadIITestActivity::class.java))
        }
    }
}