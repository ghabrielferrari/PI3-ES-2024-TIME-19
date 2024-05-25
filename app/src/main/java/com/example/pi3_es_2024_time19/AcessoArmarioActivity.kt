package com.example.pi3_es_2024_time19


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AcessoArmarioActivity : AppCompatActivity() {

    private lateinit var btn1Pessoa: Button
    private lateinit var btn2Pessoas: Button
    private lateinit var btnProximo: Button

    private var numPessoas: Int = -1
    private lateinit var nome_armario: String
    private lateinit var uid: String
    private lateinit var status: String
    private var isRented: Boolean = false
    private var preco: Double = 0.0
    private var horas: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acesso_armario)

        btn1Pessoa = findViewById(R.id.btn1pessoa)
        btn2Pessoas = findViewById(R.id.btn2pessoas)

        btn1Pessoa.setOnClickListener {
            numPessoas = 1
            Toast.makeText(this, "Selecionado: 1 Pessoa", Toast.LENGTH_SHORT).show()
            openCapturarFotoActivity()
        }

        btn2Pessoas.setOnClickListener {
            numPessoas = 2
            Toast.makeText(this, "Selecionado: 2 Pessoas", Toast.LENGTH_SHORT).show()
            openCapturarFotoActivity()
        }

        nome_armario = intent.getStringExtra("nome_armario") as String
        uid = intent.getStringExtra("uid") as String
        status = intent.getStringExtra("status") as String
        isRented = intent.getBooleanExtra("isRented", false)
        preco = intent.getDoubleExtra("preco", 0.0)
        horas = intent.getDoubleExtra("horas", 0.0)
    }

    private fun openCapturarFotoActivity() {
        val intent = Intent(this, CapturarFotoActivity::class.java)
        intent.putExtra("nome_armario", nome_armario)
        intent.putExtra("uid", uid)
        intent.putExtra("status", status)
        intent.putExtra("isRented", isRented)
        intent.putExtra("numPessoas", numPessoas)
        intent.putExtra("preco", preco)
        intent.putExtra("horas", horas)
        startActivity(intent)
    }

}