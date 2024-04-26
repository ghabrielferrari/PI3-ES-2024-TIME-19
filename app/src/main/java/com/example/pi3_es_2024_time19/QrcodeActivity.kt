package com.example.pi3_es_2024_time19

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pi3_es_2024_time19.databinding.ActivityQrcodeBinding
import com.google.android.material.color.utilities.SchemeNeutral

class QrcodeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQrcodeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // TODO stuff below

        //Botão de finalizar a locação de armário
        binding.buttonFinish.setOnClickListener {
            alertDialogSuccess(this){
                intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }


    //Função de alertaDialog que o armário foi alugado com sucesso
    private fun alertDialogSuccess (context:Context,onNeutralButtonClick:() -> Unit ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setTitle("Sucesso")
            .setMessage("Armário alugado com successo!")
            .setNeutralButton("Fechar"){ _ , _ ->
                onNeutralButtonClick()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}