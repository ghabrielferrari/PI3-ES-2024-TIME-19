package com.example.pi3_es_2024_time19

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.example.pi3_es_2024_time19.databinding.ActivityRentLockerBinding
import com.google.firebase.firestore.CollectionReference


class RentLockerActivity : AppCompatActivity() {

    private lateinit var rlTextView: TextView
    private lateinit var binding: ActivityRentLockerBinding
    private lateinit var collectionRef:CollectionReference
    private  val firestore by lazy{
        FirebaseFirestore.getInstance()
    }
    private var price = 0.0
    private var horas = 0.0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collectionRef = firestore.collection("precos_hora")

        binding = ActivityRentLockerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rlTextView = binding.tvTime

        //Text View com selecionador de tempo
        binding.tvTime.setOnClickListener {
            pickerAlertDialogTime(binding.tvTime)
        }

        //Botão de continuar operação
        binding.btnNext.setOnClickListener {
            if (horas == 0.0) {
                showVerificationDialog(
                    "Atenção",
                    "Selecione quantas horas deseja alugar",
                    "ok",
                    "",
                    ::doNothing
                )
            } else {
                alertDialogBtnNextOp(this){}
            }
        }

        //Botão de cancelar operação
        binding.btnCancel.setOnClickListener{
            alertDialogBtnCancelOp(this){}
        }
    }

    private fun loadPriceFromFirestore(selectedTime: String) {
        val field = when(selectedTime) {
            "30 minutos" -> "30 minutos"
            "1 hora" -> "1 hora"
            "2 horas" -> "2 horas"
            "3 horas" -> "3 horas"
            "+4 horas" -> "+4 horas"
            else -> ""
        }
        horas = when(selectedTime) {
            "30 minutos" -> 0.3
            "1 hora" -> 1.0
            "2 horas" -> 2.0
            "3 horas" -> 3.0
            "+4 horas" -> 4.0
            else -> 0.0
        }
        if (field.isNotEmpty()) {
            collectionRef.document("wb61LNO5gKebnBcNVICC").get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        price = documentSnapshot.getDouble(field) as Double
                        println("horas=$horas")
                        if (price != null) {
                            // Se encontrar o preço, atualiza o TextView tvRlPriceH
                            binding.tvRlPriceH.text = "R$ ${String.format("%.2f", price)}"
                        } else {
                            Toast.makeText(applicationContext, "Preço não encontrado para este tempo.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Documento não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Erro ao carregar preço.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(applicationContext, "Tempo selecionado não reconhecido.", Toast.LENGTH_SHORT).show()
        }
    }


    //Função para escolher o tempo de locação
    private fun pickerAlertDialogTime(textView : TextView){
        val timePickerOptions = arrayOf("30 minutos", "1 hora", "2 horas", "3 horas","+4 horas")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecione a quantidade horas para a locação")
            .setItems(timePickerOptions){ dialog, which ->
                val timeOption = timePickerOptions[which]
                textView.text = timeOption

                loadPriceFromFirestore(timeOption)

                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }


    //Função AlertDialog com o contrato após pressionar botão de continuar com a locação do armário
    private fun alertDialogBtnNextOp(context: Context, onAcceptClick:() -> Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("Será cobrado no cartão de crédito o valor de uma diária, e posteriormente estornado a diferença do total usado.")
            .setTitle("Contrato")
            .setPositiveButton("Aceitar") { _ , _ ->
                onAcceptClick()
                val intent = Intent(this, QrcodeActivity::class.java)
                intent.putExtra("horas", horas)
                intent.putExtra("preco", price)
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
                doNothing()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun doNothing(): Boolean {
        return true
    }

}