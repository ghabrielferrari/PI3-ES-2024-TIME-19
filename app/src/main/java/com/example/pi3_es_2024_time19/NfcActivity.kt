package com.example.pi3_es_2024_time19

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pi3_es_2024_time19.databinding.ActivityNfcBinding
// NFC
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.AndroidUiDispatcher
import com.google.common.primitives.UnsignedInts.toLong
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.hours

class NfcActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNfcBinding
    private lateinit var db: FirebaseFirestore
    // NFC
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var writeTagFilters: Array<IntentFilter>

    // extras
    private lateinit var nome_armario: String
    private lateinit var status: String
    private lateinit var uid: String
    private var isRented: Boolean = false
    private var preco: Double = 0.0
    private var horas: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore

        nome_armario = intent.getStringExtra("nome_armario") as String
        uid = intent.getStringExtra("uid") as String
        status = intent.getStringExtra("status") as String
        isRented = intent.getBooleanExtra("isRented", false)
        preco = intent.getDoubleExtra("preco", 0.0)
        horas = intent.getDoubleExtra("horas", 0.0)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        /*
        Start this activity with this pending intent, if activity already running,
        just handle the intent in override onNewIntent
        */
        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        // Intent Filters
        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                addDataType("text/plain")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Fail", e)
            }
        }
        val tagFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val techFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        writeTagFilters = arrayOf(ndefFilter, tagFilter, techFilter)
    }

    // When app is in foreground enable dispatching for scanning nfc tags
    override fun onResume() {
        super.onResume()
        enableForegroundDispatch()
    }

    // Pause scanning when app goes to background
    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    // Enables foreground dispatch on the nfc adapter for this activity
    private fun enableForegroundDispatch() {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null)
    }

    private fun getNdefMessage(): NdefMessage {
        val textBytes = "uid${uid}nome_armario${nome_armario}".toByteArray(Charset.forName("UTF-8"))
        val textRecord = NdefRecord(
            NdefRecord.TNF_WELL_KNOWN,
            NdefRecord.RTD_TEXT,
            byteArrayOf(),
            textBytes
        )
        return NdefMessage(arrayOf(textRecord))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent?.action) {
            val detectedTag: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            detectedTag?.let {
                writeTag(getNdefMessage(), it)
            }
        } else {
            //Toast.makeText(this, "INTENT = ${intent?.action}", Toast.LENGTH_SHORT).show()
            if (intent != null) {
                handleIntent(intent)
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            // NDEF FORMATTED TAGS
            NfcAdapter.ACTION_NDEF_DISCOVERED -> {
                val detectedTag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                detectedTag?.let { tag ->
                    val ndef = Ndef.get(tag)
                    ndef?.let {
                        it.connect()
                        val ndefMessage = it.ndefMessage
                        ndefMessage?.let { message ->
                            val record = message.records[0]
                            val payload = record.payload
                            val text = String(payload, Charset.forName("UTF-8"))
                            binding.tvStatus.text = "Ndef text: $text"
                            Toast.makeText(this, "Read content: $text", Toast.LENGTH_SHORT).show()
                        }
                        it.close()
                    }
                }
            }
            // EMPTY TAGS
            null -> {
                //val detectedTag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                addLocacaoToFirestore()
                //writeTag(getNdefMessage(), detectedTag)
            }
        }

    }

    // TODO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private fun addLocacaoToFirestore() {
        val date = Date() // Current date and time

        val calendar = Calendar.getInstance()
        calendar.time = date
        if (horas >= 1) {
            calendar.add(Calendar.HOUR_OF_DAY, horas.toInt())
        } else {
            calendar.add(Calendar.MINUTE, (horas*60).toInt())
        }
        val newDate = calendar.time

        println("Original Date: $date")
        println("New Date after adding $horas hours: $newDate")

        //val timestampEnd = java.sql.Timestamp(date)

        db.collection("locacao")
            .add(hashMapOf(
                "id_armario" to "",
                "nome_armario" to nome_armario,
                "preco" to preco,
                "timestamp_inicio" to Timestamp.now(),
                "timestamp_fim" to Timestamp(newDate),
                "uid" to uid)
            )
            .addOnSuccessListener {
                showVerificationDialog(
                    "Sucesso!",
                    "Armário alugado com sucesso",
                    "ok",
                    "",
                    ::openMainActivity,
                )
            }
            .addOnFailureListener {
                showVerificationDialog(
                    "Erro!",
                    "Não foi possível alugar o armario, tente novamente",
                    "ok",
                    "",
                    ::openMainActivity,
                )
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
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun doNothing(): Boolean {
        return true
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun writeTag(ndefMessage: NdefMessage, tag: Tag): Boolean {
        Toast.makeText(this, "Writing to tag...", Toast.LENGTH_SHORT).show()
        return true
        /* Estou parando a execuçao pois a tag nao suporta Ndef ent retorna null o objeto Tag
        que seria usado para escrever NdefMessage e NdefRecord, portanto simulando a leitura e escrita */
        try {
            val ndef = Ndef.get(tag)
            ndef?.let {
                it.connect()
                if (!it.isWritable) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show()
                    return false
                }
                if (it.maxSize < ndefMessage.toByteArray().size) {
                    Toast.makeText(this, "Tag doesn't have enough space!", Toast.LENGTH_SHORT).show()
                    return false
                }
                it.writeNdefMessage(ndefMessage)
                it.close()
                Toast.makeText(this, "Tag written successfully!", Toast.LENGTH_SHORT).show()
                return true
            }
            val ndefFormatable = NdefFormatable.get(tag)
            ndefFormatable?.let {
                it.connect()
                it.format(ndefMessage)
                it.close()
                Toast.makeText(this, "Tag formatted and written successfully!", Toast.LENGTH_SHORT).show()
                return true
            }
            Toast.makeText(this, "NFC tag is not NDEF compatible!", Toast.LENGTH_SHORT).show()
            return false
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to write tag", Toast.LENGTH_SHORT).show()
            Log.e("writeTag", "Failed to write tag", e)
        }
        return false
    }



}