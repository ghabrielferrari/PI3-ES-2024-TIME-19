package com.example.pi3_es_2024_time19.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Activity.showToast(message: String){
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

/*
fun Activity.showSnack(view: View ,message: String){
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setAction("OKAY", {}).show()
}*/
