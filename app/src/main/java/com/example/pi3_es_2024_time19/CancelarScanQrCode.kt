package com.example.pi3_es_2024_time19

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityCancelarScanQrCodeBinding
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.ExecutorService

class CancelarScanQrCode : AppCompatActivity() {
    private lateinit var binding: ActivityCancelarScanQrCodeBinding
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var auth: FirebaseAuth
    private lateinit var userData: UserData
    private lateinit var db: FirebaseFirestore
    private var isManager = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelarScanQrCodeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_cancelar_scan_qr_code)

        auth = Firebase.auth
        getUserData()
    }

    private fun startCameraControllerWithQrCodeAnalyzer() {
        val previewView: PreviewView = binding.viewFinder
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        previewView.controller = cameraController

        // create BarcodeScanner object
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        val barcodeScanner = BarcodeScanning.getClient(options)

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(this),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(this)
            ) { result: MlKitAnalyzer.Result? ->
                // The value of result.getResult(barcodeScanner) can be used directly for drawing UI overlay.
                // Process the result to get the barcodes
                val barcodes = result?.getValue(barcodeScanner)

                // Check if barcodes are not null and process each barcode
                if (barcodes != null) {
                    for (barcode in barcodes) {
                        // Extract the raw value of the barcode
                        val rawValue = barcode.rawValue
                        if (rawValue != null) {
                            // Do something with the extracted string value from the QR code
                            Log.d("cancelar scan qrcode", "QR Code Value: $rawValue")
                            val uid = rawValue.toString()
                            val intent = Intent(this, CancelarArmarios::class.java)
                            intent.putExtra("uid", uid)
                            startActivity(intent)
                        }
                    }
                }

            }
        )

    }

    private fun getUserData() {
        db.collection("user_data")
            .whereEqualTo("uid", "${auth.currentUser?.uid}")
            .get()
            .addOnSuccessListener{ documents ->
                if (documents.isEmpty) {
                    Log.d("QUERY FAILED", "Size of query is zero")
                } else {
                    for (doc in documents) {
                        userData = doc.toObject(UserData::class.java)
                        isManager = doc.get("manager") as Boolean
                        Log.d("USER_DATA", "$userData")
                        if (isManager) {
                            startCameraControllerWithQrCodeAnalyzer()
                        } else {
                            val intent = Intent(this, CancelarGenQrCodeActivity::class.java)
                            startActivity(intent)
                        }
                        break
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ocorreu, verifique conexão e tente novamente!", Toast.LENGTH_SHORT).show()
            }
    }
}