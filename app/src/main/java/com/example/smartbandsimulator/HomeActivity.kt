package com.example.smartbandsimulator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartbandsimulator.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // vechea logică de navigare
        binding.btnHealthCheck.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnFitness.setOnClickListener {
            startActivity(Intent(this, FitnessActivity::class.java))
        }
        binding.btnVoiceAssistant.setOnClickListener {
            startActivity(Intent(this, VoiceAssistantActivity::class.java))
        }
    }
}
