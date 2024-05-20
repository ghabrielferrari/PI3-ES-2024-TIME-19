package com.example.pi3_es_2024_time19

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationProvider
import android.os.Bundle
import android.telephony.ServiceState
import android.view.textservice.TextServicesManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityGetLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.Intent
import android.view.View

class GetLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetLocationBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setContentView(binding.root)

        setFusedLocationProviderClient()

        checkLocationPermission()
    }

    private fun setBinding() {
        binding = ActivityGetLocationBinding.inflate(layoutInflater)
    }

    private fun setFusedLocationProviderClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            binding.statusText.text = "Volte Para a tela anterior e clique o botão Alugar Novo Armario novamente"
        } else {
            getLastKnownLocation()
        }
    }

    private fun getLastKnownLocation() {
        showLoading()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            hideLoading()
            return
        }

        fusedLocationProviderClient.lastLocation
            .addOnCompleteListener { task ->
                hideLoading()
                val location = task.result
                val latitude = location.latitude
                val longitude = location.longitude
                println("user location: ($latitude , $longitude)")
                binding.statusText.text = "user location: ($latitude , $longitude)"
            }
            .addOnFailureListener {
                hideLoading()
                println("Failed to get location :(")
                binding.statusText.text = "Failed to get location :("
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getLastKnownLocation()
    }

    private fun openQrCodeActivity() {
        val intent = Intent(this, QrcodeActivity::class.java)
        startActivity(intent)
    }

    private fun showLoading() {
        binding.loadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingSpinner.visibility = View.INVISIBLE
    }
}