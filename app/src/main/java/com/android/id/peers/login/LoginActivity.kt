package com.android.id.peers.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.forgot.ForgotPasswordActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.login)
        val forgot = findViewById<TextView>(R.id.forgot_button)
        login.setOnClickListener { login(it) }
        forgot.setOnClickListener { forgotPassword(it) }
    }

    private fun login(view: View) {
        val username = findViewById<TextInputEditText>(R.id.username)
        val password = findViewById<TextInputEditText>(R.id.password)
        val usernameC = findViewById<TextInputLayout>(R.id.username_container)
        val passwordC = findViewById<TextInputLayout>(R.id.password_container)
        if(username.text.toString().isEmpty()) {
            usernameC.error = "Username cannot be empty"
        }
        if(password.text.toString().isEmpty()) {
            passwordC.error = "Password cannot be empty"
        }
        if(!username.text.toString().isEmpty() && !password.text.toString().isEmpty()){

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            authenticate(username.text.toString(), password.text.toString())
            val preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
            preferences.edit().putString("username", username.text.toString()).apply()
            finish()
        }
    }

    private fun forgotPassword(view: View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun authenticate(username: String, password: String) {
        val preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
        preferences.edit().putString("username", "Test").apply()
    }
}
