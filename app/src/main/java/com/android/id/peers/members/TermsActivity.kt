package com.android.id.peers.members

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.*
import android.widget.Button
import android.widget.TextView
import com.android.id.peers.R

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels

        val termsText = findViewById<TextView>(R.id.terms_text)
        val termsTextArea = findViewById<TextView>(R.id.terms_text_area)
        val lanjutkan = findViewById<Button>(R.id.lanjutkan)
        Log.d("TermsActivity", String.format("%d", termsTextArea.layoutParams.height))

        Log.d("TermsActivity", "GG" + String.format("%d", resources.getDimension(R.dimen.activity_vertical_margin).toInt()))
        termsTextArea.movementMethod = ScrollingMovementMethod()
        termsTextArea.requestLayout()

        lanjutkan.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                lanjutkan.viewTreeObserver.removeOnGlobalLayoutListener(this)
                Log.d("TermsActivity", "HAHAHA" + String.format("%d", termsText.height))
                val temp = height - termsText.height - lanjutkan.height - 6 * resources.getDimension(R.dimen.activity_vertical_margin).toInt()
                termsTextArea.layoutParams.height = temp
                termsTextArea.requestLayout()
                Log.d("TermsActivity", "WKWKWK" + String.format("%d", termsTextArea.layoutParams.height))
            }
        })
    }
}
