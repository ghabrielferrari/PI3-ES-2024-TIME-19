package com.example.pi3_es_2024_time19.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Activity.showToast(message: String){
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

private fun showDialog(context: Context,
                       title: String,
                       message: String,
                       positiveBtnText: String,
                       negativeBtnText: String
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveBtnText) { _, _ -> }
        .setNegativeButton(negativeBtnText) { dialogInterface: DialogInterface, i: Int -> }
        .show()
}

/*
fun Activity.showSnack(view: View ,message: String){
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setAction("OKAY", {}).show()
}*/
