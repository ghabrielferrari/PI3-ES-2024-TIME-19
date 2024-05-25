package com.example.pi3_es_2024_time19

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityVerFotosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage

class VerFotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerFotosBinding
    private lateinit var auth: FirebaseAuth
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerFotosBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_ver_fotos)

        auth = Firebase.auth

        retrieveImagesFromStorage(auth.currentUser?.uid.toString())
    }

    // Function to retrieve 2 images from Firebase Storage
    fun retrieveImagesFromStorage(folderPath: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        // Reference to the folder in Firebase Storage
        val folderRef = storageRef.child(folderPath)

        // Retrieve the first image
        val imageRef1 = folderRef.child("2024-05-145-12-46-42.jpg")
        imageRef1.downloadUrl.addOnSuccessListener { uri1 ->
            // Handle successful download
            val imageUrl1 = uri1
            println("Image 1 URL: $imageUrl1")
            binding.img1.setImageURI(imageUrl1)
            count++
            if (count == 2) {
                openCancelarArmariosActivity()
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
            println("Error downloading image 1: $exception")
        }

        // Retrieve the second image
        val imageRef2 = folderRef.child("2024-05-145-11-09-35.jpg")
        imageRef2.downloadUrl.addOnSuccessListener { uri2 ->
            // Handle successful download
            val imageUrl2 = uri2
            println("Image 2 URL: $imageUrl2")
            binding.img2.setImageURI(imageUrl2)
            count++
            if (count == 2) {
                openCancelarArmariosActivity()
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
            println("Error downloading image 2: $exception")
        }
    }

    private fun openCancelarArmariosActivity() {
        val intent = Intent(this, CancelarArmarios::class.java)
        startActivity(intent)
    }
}