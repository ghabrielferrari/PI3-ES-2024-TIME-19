package com.example.pi3_es_2024_time19

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityCapturarFotoBinding
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService

class CapturarFotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCapturarFotoBinding
    private lateinit var cameraController: LifecycleCameraController
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageCapture: ImageCapture
    private val TAG = "CapturarFotoActivity"

    private lateinit var uid: String
    private lateinit var nome_armario: String
    private lateinit var status: String
    private var isRented: Boolean = false
    private var numPessoas: Int = 1
    private var preco = 0.0
    private var horas = 0.0
    private var photosLeftCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapturarFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nome_armario = intent.getStringExtra("nome_armario") as String
        uid = intent.getStringExtra("uid") as String
        status = intent.getStringExtra("status") as String
        isRented = intent.getBooleanExtra("isRented", false)
        preco = intent.getDoubleExtra("preco", 0.0)
        horas = intent.getDoubleExtra("horas", 0.0)
        numPessoas = intent.getIntExtra("numPessoas", 1)
        photosLeftCount = numPessoas

        println("UID >>> $uid")
        println("NOME_ARMARIO >>> $nome_armario")

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, AcessoArmarioActivity::class.java)
            intent.putExtra("nome_armario", nome_armario)
            intent.putExtra("uid", uid)
            intent.putExtra("status", status)
            intent.putExtra("isRented", isRented)
            intent.putExtra("preco", preco)
            intent.putExtra("horas", horas)
            startActivity(intent)
        }
        binding.btnImageCapture.setOnClickListener {
            takePhotoAndSaveToFirebase()
            binding.btnImageCapture.isEnabled = false
        }

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Bind lifecycle of cameras to lifecycle owner (this activity)
            cameraProvider = cameraProviderFuture.get()

            // Create preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll() // Unbind use cases before rebinding
                cameraProvider.bindToLifecycle( // Bind use cases to camera
                    this, cameraSelector, preview, imageCapture
                )
            } catch(exc: Exception) {
                Log.d("CAMERA_X", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhotoAndSaveToFirebase() {
        // Create a file to save the image
        val outputDirectory = getOutputDirectory()
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("YYYY-MM-DD-hh-mm-ss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Take the picture
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Image saved successfully, now upload it to Firebase Storage
                    // Keep taking pictures until max number of people
                    cameraProvider.unbindAll() // Stop camera
                    if (photosLeftCount > 0) {
                        uploadImageToFirebase(photoFile)
                        showVerificationDialog(
                            "Sucesso",
                            "Foto da pessoa ${numPessoas - photosLeftCount + 1} foi enviada!",
                            "ok",
                            "",
                            ::startCamera // Restart camera
                        )
                        photosLeftCount--
                        binding.tvStatus.text = "Tire a foto da pessoa ${numPessoas - photosLeftCount + 1}"
                        if (photosLeftCount == 0) {
                            showVerificationDialog(
                                "Sucesso",
                                "Todas as fotos foram tiradas!",
                                "ok",
                                "",
                                ::openNfcActivity
                            )
                            binding.tvStatus.text = "Todas as fotos foram tiradas!"
                        } else {
                            binding.btnImageCapture.isEnabled = true
                        }
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    // Handle any errors
                    cameraProvider.unbindAll() // Stop camera
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    showVerificationDialog(
                        "Erro na captura",
                        "Não foi possível capturar a foto :(",
                        "ok",
                        "",
                        ::startCamera
                    )
                }
            }
        )


    }

    private fun uploadImageToFirebase(file: File) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val fileRef = storageRef.child("$uid/${file.name}")

        val uploadTask = fileRef.putFile(Uri.fromFile(file))
        uploadTask.addOnSuccessListener {
            // Image uploaded successfully
            Log.d(TAG, "Image upload successful")
        }.addOnFailureListener { exception ->
            // Handle unsuccessful uploads
            cameraProvider.unbindAll() // Stop camera
            Log.e(TAG, "Image upload failed", exception)
            showVerificationDialog(
                "Erro de conexão",
                "Não foi possível enviar e salvar a foto :(, verifique sua internet",
                "ok",
                "",
                ::startCamera
            )
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
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
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun doNothing(): Boolean {
        return true
    }

    private fun openNfcActivity() {
        val intent = Intent(this, NfcActivity::class.java)
        intent.putExtra("nome_armario", nome_armario)
        intent.putExtra("uid", uid)
        intent.putExtra("status", status)
        intent.putExtra("isRented", isRented)
        intent.putExtra("preco", preco)
        intent.putExtra("horas", horas)
        startActivity(intent)
    }
}