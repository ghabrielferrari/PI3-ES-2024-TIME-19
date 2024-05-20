package com.example.pi3_es_2024_time19.fragments

import android.media.MediaPlayer
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pi3_es_2024_time19.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var mediaPlayer: MediaPlayer

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        // Set Google Maps object.
        this.googleMap = googleMap

        addExistingLocations()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playSound(R.raw.mapa)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun addExistingLocations() {
        // Localizacoes de armarios
        val campinas = LatLng(-22.9051, -47.0613)
        val pucc = LatLng(-22.83389728110038, -47.05269190905707)

        val marker = googleMap.addMarker(MarkerOptions()
            .position(campinas)
            .title("Locker in Campinas - Perto da catedral"))
            ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.lock_marker))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(campinas))
        //onMarkerClick(marker as Marker)

        val marker2 = googleMap.addMarker(MarkerOptions()
            .position(pucc)
            .title("Locker in Pontíficia Universidade Católica - No prédio do H15"))
            ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.lock_marker))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pucc))
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        playSound(R.raw.marker_click_voice)
        return false
    }

    private fun playSound(rawFile: Int) {
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(context, rawFile)
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        } else {
            mediaPlayer.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

}