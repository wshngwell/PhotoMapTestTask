package com.example.map.presentation

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogManager {
    fun locationSettingsDialog(context: Context, onClick:()->Unit) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Включить GPS")
        dialog.setMessage("GPS выключен, включить его?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok") { _, _ ->
            onClick()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }


}