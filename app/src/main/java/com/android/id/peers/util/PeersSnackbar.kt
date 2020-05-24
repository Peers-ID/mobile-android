package com.android.id.peers.util

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class PeersSnackbar {
    companion object {
        fun popUpSnack(view: View, message: String){
            val rootView = view.findViewById<View>(android.R.id.content)
//        val rootView = this.window.decorView.findViewById<View>(android.R.id.content)
            val snackBar = Snackbar.make(rootView, "",
                Snackbar.LENGTH_LONG).setAction("Action", null)
            snackBar.setActionTextColor(Color.BLUE)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.BLACK)
            val textView =
                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.text = message
            textView.setTextColor(Color.WHITE)
//        Log.d("LoanApplication", "asd")
            snackBar.show()
        }
    }
}