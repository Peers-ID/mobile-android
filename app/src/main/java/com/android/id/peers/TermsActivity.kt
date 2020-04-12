package com.android.id.peers

import android.annotation.SuppressLint
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : AppCompatActivity() {

    var noHP = ""

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        noHP = (intent.getStringExtra("hand_phone"))!!

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels

//        Log.d("TermsActivity", String.format("%d", termsTextArea.layoutParams.height))

//        Log.d("TermsActivity", "GG" + String.format("%d", resources.getDimension(R.dimen.activity_vertical_margin).toInt()))
        terms_text_area.movementMethod = ScrollingMovementMethod()
        terms_text_area.requestLayout()

        lanjutkan.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                lanjutkan.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                Log.d("TermsActivity", "HAHAHA" + String.format("%d", termsText.height))
                val temp = height - terms_text.height - agree.height - lanjutkan.height - 7 * resources.getDimension(R.dimen.activity_vertical_margin).toInt() - resources.getDimension(R.dimen.margin_between).toInt()
                terms_text_area.layoutParams.height = temp
                terms_text_area.requestLayout()
//                Log.d("TermsActivity", "WKWKWK" + String.format("%d", termsTextArea.layoutParams.height))
            }
        })

        lanjutkan.setOnClickListener {
            if(!agree.isChecked) {
                agree.supportButtonTintList = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
                agree.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }else {
                val intent = Intent(this, VerificationActivity::class.java)
                intent.putExtra("hand_phone", noHP)
                startActivity(intent)
            }
        }
    }
}
