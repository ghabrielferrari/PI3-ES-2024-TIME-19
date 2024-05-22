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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.math.abs

class GetLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetLocationBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val locations = mutableListOf<LatLng>()
    private lateinit var userLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup binding
        binding = ActivityGetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        // Setup FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
            binding.statusText.text = "Volte Para a tela anterior e clique o botão Alugar Novo Armario novamente"
        } else {
            getLastKnownLocation()
        }
    }

    private fun getLastKnownLocation() {
        showLoading()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
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
                userLocation = LatLng(latitude, longitude)
                binding.statusText.text = "Localização adquirida"
                getLocksLocations()
            }
            .addOnFailureListener {
                hideLoading()
                println("Failed to get location :(")
                binding.statusText.text = "Failed to get location, Check internet"
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

    private fun getLocksLocations() {
        db.collection("locais")
            .get()
            .addOnSuccessListener {locs ->
                for (loc in locs) {
                    locations.add(LatLng(
                        loc.get("lat") as Double,
                        loc.get("long") as Double
                        )
                    )
                }
                if (locs.isEmpty) {
                    binding.statusText.text = "Ainda não há locais de armários para serem alugados"
                }
                var podeAlugar = false
                for (loc in locations) {
                    if (abs(userLocation.latitude - loc.latitude) <= 0.005 && abs(userLocation.longitude - loc.longitude) <= 0.005) {
                        podeAlugar = true
                        break
                    }
                }
                if (podeAlugar) {
                    openQrCodeActivity()
                } else {
                    binding.statusText.text = "Você está longe demais de um armário :(, va até um dos nossos armários é só pesquisar no mapa do própio aplicativo para ver os locais disponíveis!"
                }
            }
            .addOnFailureListener {
                binding.statusText.text = "Failed to get locations :(, check internet and try again later"
            }
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