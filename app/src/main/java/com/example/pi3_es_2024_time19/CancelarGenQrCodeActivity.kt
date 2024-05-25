package com.example.pi3_es_2024_time19

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityCancelarGenQrCodeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class CancelarGenQrCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCancelarGenQrCodeBinding
    private lateinit var auth: FirebaseAuth
    private val width = 200
    private val height = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelarGenQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        //Botão de finalizar a locação de armário
        binding.buttonFinish.setOnClickListener {
            alertDialogSuccess(this){
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        val text: String = "${auth.currentUser?.uid}"
        println(text)
        val bitmap = generateQRCode(text, width, height)

        if (bitmap != null) {
            println("QR CODE GERADO")
            binding.imageViewQRCode.setImageBitmap(bitmap)
        }

    }

    fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    //Função de alertaDialog que o armário foi alugado com sucesso
    private fun alertDialogSuccess (context: Context, onNeutralButtonClick:() -> Unit ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setTitle("Observação")
            .setMessage("Armários seram cancelados pelo gerente, caso ainda nao foi cancelado mostre o QR code para o gerente novamente!")
            .setNeutralButton("Fechar"){ _ , _ ->
                onNeutralButtonClick()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}