package com.android.id.peers.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.id.peers.R
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject

const val API_HOSTNAME = "http://13.212.188.255/v1/"

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
        var allTrue = true
        if(username.text.toString().isEmpty()) {
            username.error = "Alamat Email tidak boleh kosong"
            allTrue = false
        }
//        if(birthDate.text.toString().isEmpty()) {
//            birthDateC.error = "Birth date cannot be empty"
//            allTrue = false
//        }
        if (allTrue) {
            forgotPassword(view, username.text.toString())
        }
    }

    private fun forgotPassword(view: View, username: String) {
        val url = "${API_HOSTNAME}forgot_password"

        val params = HashMap<String, String>()
//        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        params["email"] = username
//        params["token"] = preferences.getString("token", null)!!

        val parameters = JSONObject(params as Map<*, *>);
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, parameters,
            Response.Listener { response ->
                Log.d("ForgotPasswordActivity", response.toString())
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val responseStatus = jsonObj.getString("status")
//                Log.d("LoginActivity", strResp)
                if (responseStatus.toInt() == 400 || responseStatus.toInt() == 401) {
                    PeersSnackbar.popUpSnack(this.window.decorView, "Input tidak valid!")
                } else if (responseStatus.toInt() == 201) {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("message", "Link untuk mereset kata sandi telah dikirim")
                    startActivity(intent)
                    finishAffinity()
                }
            },
            Response.ErrorListener { error ->
                Log.e("ChangePasswordActivity", error.toString())
            }
        )

        VolleyRequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun popUpSnack(view: View, message: String){
        val rootView = this.window.decorView.findViewById<View>(android.R.id.content)
        val snackBar = Snackbar.make(rootView, "",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLUE)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.GRAY)
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.text = message
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    private fun login(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
