package com.android.id.peers

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels

        val termsText = findViewById<TextView>(R.id.terms_text)
        val termsTextArea = findViewById<TextView>(R.id.terms_text_area)
        val agree = findViewById<AppCompatCheckBox>(R.id.agree)
        val lanjutkan = findViewById<Button>(R.id.lanjutkan)
        Log.d("TermsActivity", String.format("%d", termsTextArea.layoutParams.height))

        Log.d("TermsActivity", "GG" + String.format("%d", resources.getDimension(R.dimen.activity_vertical_margin).toInt()))
        termsTextArea.movementMethod = ScrollingMovementMethod()
        termsTextArea.requestLayout()

        lanjutkan.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                lanjutkan.viewTreeObserver.removeOnGlobalLayoutListener(this)
                Log.d("TermsActivity", "HAHAHA" + String.format("%d", termsText.height))
                val temp = height - termsText.height - agree.height - lanjutkan.height - 7 * resources.getDimension(R.dimen.activity_vertical_margin).toInt() - resources.getDimension(R.dimen.margin_between).toInt()
                termsTextArea.layoutParams.height = temp
                termsTextArea.requestLayout()
                Log.d("TermsActivity", "WKWKWK" + String.format("%d", termsTextArea.layoutParams.height))
            }
        })

        lanjutkan.setOnClickListener {
            if(!agree.isChecked) {
                agree.supportButtonTintList = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
                agree.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }
    }
}
