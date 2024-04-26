package com.example.pi3_es_2024_time19

import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.location.GnssAntennaInfo.Listener
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog
import com.example.pi3_es_2024_time19.databinding.ActivityRentLockerBinding

class RentLockerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRentLockerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRentLockerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Text View com selecionador de tempo
        binding.tvTime.setOnClickListener {
            pickerAlertDialogTime(binding.tvTime)
        }


        //Botão de continuar operação
        binding.btnNext.setOnClickListener {
            alertDialogBtnNextOp(this){}
        }

        //Botão de cancelar operação
        binding.btnCancel.setOnClickListener{
            alertDialogBtnCancelOp(this){}
        }
    }


    //Função para escolher o tempo de locação
    private fun pickerAlertDialogTime(textView : TextView){
        val timePickerOptions = arrayOf("30 minutos", "1 hora ", "2 horas ", "3 horas ","+4 horas")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecione a quantidade horas para a locação")
            .setItems(timePickerOptions){ dialog, which ->
                val timeOption = timePickerOptions[which]
                textView.text = timeOption
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }


    //Função AlertDialog com o contrato após pressionar botão de continuar com a locação do armário
    private fun alertDialogBtnNextOp(context: Context, onAcceptClick:() -> Unit){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("Será cobrado no cartão de crédito o valor de uma diária, e posteriormente estornado a diferença do total usado.")
            .setTitle("Contrato")
            .setPositiveButton("Aceitar") { _ , _ ->
                onAcceptClick()
                val intent = Intent(this,QrcodeActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Negar") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Função AlertDialog para cancelar a operação de alugar armário
    private  fun alertDialogBtnCancelOp(context:Context,onContinueClick:() -> Unit){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("Você está prestes a cancelar a operação, deseja continuar com o cancelamento?")
            .setTitle("Aviso!")
            .setPositiveButton("Continuar"){ _ , _ ->
                onContinueClick()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Cancelar"){ dialog , _ ->
                dialog.dismiss()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}