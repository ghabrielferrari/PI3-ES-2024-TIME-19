package com.example.pi3_es_2024_time19

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityRentLockerBinding

class RentLockerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRentLockerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentLockerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}