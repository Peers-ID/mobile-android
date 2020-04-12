package com.android.id.peers.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.id.peers.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        val submit = findViewById<Button>(R.id.submit)
        val login = findViewById<TextView>(R.id.login_button)
        submit.setOnClickListener { submit(it) }
        login.setOnClickListener { login(it) }
    }

    private fun submit(view: View) {
        val username = findViewById<TextInputEditText>(R.id.username)
        val birthDate = findViewById<TextInputEditText>(R.id.birth_date)
        val usernameC = findViewById<TextInputLayout>(R.id.username_container)
        val birthDateC = findViewById<TextInputLayout>(R.id.birth_date_container)
        if(username.text.toString().isEmpty()) {
            usernameC.error = "Username cannot be empty"
        }
        if(birthDate.text.toString().isEmpty()) {
            birthDateC.error = "Birth date cannot be empty"
        }
    }

    private fun login(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
