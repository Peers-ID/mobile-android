package com.android.id.peers.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.id.peers.R
import com.android.id.peers.SplashScreenActivity
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
//const val API_HOSTNAME = "http://api.peers.id/api/v1/"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        val login = findViewById<Button>(R.id.login)
        val forgot = findViewById<TextView>(R.id.forgot_button)
        login.setOnClickListener { login(it) }
        forgot.setOnClickListener { forgotPassword(it) }
    }

    private fun login(view: View) {
        var allTrue = true
        if(username.text.toString().isEmpty()) {
            username.error = "Alamat Email tidak boleh kosong"
            allTrue = false
        }
        if(password.text.toString().isEmpty()) {
            password.error = "Kata Sandi tidak boleh kosong"
            allTrue = false
        }
        if(allTrue){
            authenticate(view, username.text.toString(), password.text.toString())
        }
    }

    private fun forgotPassword(view: View) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }



    private fun authenticate(view: View, username: String, password: String) {
        val url = "${API_HOSTNAME}login"

        val params = HashMap<String, String>()
        params["email"] = username
        params["password"] = password

        val mainView = this.window.decorView

        val parameters = JSONObject(params as Map<*, *>)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, parameters,
            Response.Listener { response ->
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val loginStatus = jsonObj.getString("status")
                Log.d("LoginActivity", strResp)
                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
                    PeersSnackbar.popUpSnack(mainView, "Login Failed!")
                } else if (loginStatus.toInt() == 201) {
                    val dataJsonObj = jsonObj.getJSONObject("data")
                    val token = dataJsonObj.getString("token")
                    val userJsonObj = dataJsonObj.getJSONObject("user")
                    val id = userJsonObj.getString("id")
                    val koperasiId = userJsonObj.getString("koperasi_id")
                    val fullName = userJsonObj.getString("fullname")
                    val phoneMobile = userJsonObj.getString("phone_mobile")
                    val birthDate = userJsonObj.getString("birthdate")
                    val email = userJsonObj.getString("email")
                    val role = userJsonObj.getString("role")
                    val akId = userJsonObj.getString("ak_id")
                    val status = userJsonObj.getString("status")

                    if (role != "AO/CMO/Sales") {
                        PeersSnackbar.popUpSnack(mainView, "Only AO can access!!")
                    } else {
                        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

                        preferences.edit().putString("token", token).apply()
                        preferences.edit().putInt("id", id.toInt()).apply()
                        preferences.edit().putInt("koperasi_id", koperasiId.toInt()).apply()
                        preferences.edit().putString("full_name", fullName).apply()
                        preferences.edit().putString("phone_mobile", phoneMobile).apply()
                        preferences.edit().putString("birth_date", birthDate).apply()
                        preferences.edit().putString("email", email).apply()
                        preferences.edit().putString("password", password).apply()
                        preferences.edit().putString("role", role).apply()
                        preferences.edit().putInt("ak_id", akId.toInt()).apply()
                        preferences.edit().putString("status", status).apply()
                        val intent = Intent(this, SplashScreenActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
//                    popUpSnack(view, "Login Success!")
                }
            },
            Response.ErrorListener { error ->
                Log.e("LoginActivity", error.toString())
            }
        )

        VolleyRequestSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
