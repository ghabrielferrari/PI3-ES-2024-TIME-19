package com.example.pi3_es_2024_time19


import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AcessoArmarioActivity : AppCompatActivity() {

    private lateinit var btn1Pessoa: Button
    private lateinit var btn2Pessoas: Button
    private lateinit var btnProximo: Button
    private var numPessoas: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acesso_armario)

        btn1Pessoa = findViewById(R.id.btn1pessoa)
        btn2Pessoas = findViewById(R.id.btn2pessoas)
        btnProximo = findViewById(R.id.btnProximo)

        btn1Pessoa.setOnClickListener {
            numPessoas = 1
            Toast.makeText(this, "Selecionado: 1 Pessoa", Toast.LENGTH_SHORT).show()
        }

        btn2Pessoas.setOnClickListener {
            numPessoas = 2
            Toast.makeText(this, "Selecionado: 2 Pessoas", Toast.LENGTH_SHORT).show()
        }

        /*btnProximo.setOnClickListener {
            if (numPessoas == -1) {
                Toast.makeText(this, "Por favor, selecione uma opção.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, TirarFotoActivity::class.java).apply {
                    putExtra("numPessoas", numPessoas)
                }
                startActivity(intent)
            }
        }*/
    }
}