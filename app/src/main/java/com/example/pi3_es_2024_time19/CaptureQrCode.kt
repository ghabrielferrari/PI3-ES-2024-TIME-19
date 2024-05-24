package com.example.pi3_es_2024_time19

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityCaptureQrCodeBinding
import com.example.pi3_es_2024_time19.databinding.ActivityGetLocationBinding
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.camera2.internal.annotation.CameraExecutor
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pi3_es_2024_time19.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService

class CaptureQrCode : AppCompatActivity() {
    private lateinit var binding: ActivityCaptureQrCodeBinding
    // Camera variables
    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
    )
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var userData: UserData
    private lateinit var db: FirebaseFirestore
    private val TAG = "CaptureQrCode"
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCameraControllerWithQrCodeAnalyzer()
        } else {
            requestPermissions()
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Setup firebase firestore
        db = Firebase.firestore
    }

    private fun takePhoto() {}

    private fun captureVideo() {}

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Bind lifecycle of cameras to lifecycle owner (this activity)
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Create preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll() // Unbind use cases before rebinding
                cameraProvider.bindToLifecycle( // Bind use cases to camera
                    this, cameraSelector, preview
                )
            } catch(exc: Exception) {
                Log.d("CAMERA_X", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
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
                COORDINATE_SYSTEM_VIEW_REFERENCED,
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
                            Log.d(TAG, "QR Code Value: $rawValue")
                            if (rawValue.toString().substring(0,3).equals("uid")) {
                                val uid = rawValue.toString().substring(3)
                                Log.d(TAG, "UID=$uid")
                                getUserData(uid)
                                cameraController.unbind()
                                break
                            }
                        }
                    }
                }

            }
        )

    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()

        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach { permission ->
                println(permission)
                if (permission.key in REQUIRED_PERMISSIONS && permission.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,"Permission request denied", Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    private fun showVerificationDialog(title: String, message: String, positiveBtnText: String, negativeBtnText: String, positiveAction: () -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage(message)
            .setTitle(title)
            .setPositiveButton(positiveBtnText) { _, _ ->
                positiveAction()
            }
            .setNegativeButton(negativeBtnText) { dialog, _ ->
                dialog.dismiss()
                startCameraControllerWithQrCodeAnalyzer()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun doNothing(): Boolean {
        return true
    }

    private fun getUserData(uid: String) {
        showLoading()
        db.collection("user_data")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener{ documents ->
                hideLoading()
                if (documents.isEmpty) {
                    playSound(R.raw.w98error_sfx)
                    showVerificationDialog(
                        "Código inválido!",
                        "Tente novamente",
                        "ok",
                        "",
                        ::startCameraControllerWithQrCodeAnalyzer
                    )
                } else {
                    for (doc in documents) {
                        userData = doc.toObject(UserData::class.java)
                        Log.d(TAG, "$userData")
                        playSound(R.raw.coin_sfx)
                        showVerificationDialog(
                                    "Você tem certeza?",
                                    "Deseja alugar armário para ${userData.nome_completo}",
                            "sim",
                            "cancelar",
                            ::goToMainActivity
                        )
                        break
                    }
                }
            }
            .addOnFailureListener {
                hideLoading()
                playSound(R.raw.w98error_sfx)
                showVerificationDialog(
                    "Erro de Conexão!",
                    "Verifique sua internet e tente novamente mais tarde :(",
                    "ok",
                    "",
                    ::doNothing
                )
            }
    }

    private fun showLoading() {
        binding.loadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingSpinner.visibility = View.INVISIBLE
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun playSound(rawFile: Int) {
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(this, rawFile)
        }
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }
        mediaPlayer.start()
    }

}