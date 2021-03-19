package com.android.id.peers.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.id.peers.R
import com.android.id.peers.SplashScreenActivity
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.response.LoginResponse
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.id.peers.util.repository.ApiRepository
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Field

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
        val params = HashMap<String, String>()
        params["email"] = username
        params["password"] = password

        val mainView = this.window.decorView

        val apiRepository = ApiRepository.create()

        apiRepository.login(params).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {

                val data = response.body()
                data.let{
                    val loginStatus = it!!.status

                    Log.d("LoginActivity", it.toString())
                    if (loginStatus == 400 || loginStatus == 401) {
                        PeersSnackbar.popUpSnack(mainView, "Login Failed!")
                    } else if (loginStatus == 201) {
                        val dataJsonObj = it!!.data
                        val token = dataJsonObj.token
                        val user = dataJsonObj.user

                        if (user.role != "AO/CMO/Sales") {
                            PeersSnackbar.popUpSnack(mainView, "Only AO can access!!")
                        } else {
                            val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)

                            preferences.edit().putString("token", token).apply()
                            preferences.edit().putInt("id", user.id).apply()
                            preferences.edit().putInt("koperasi_id", user.koperasi_id).apply()
                            preferences.edit().putString("full_name", user.fullname).apply()
                            preferences.edit().putString("phone_mobile", user.phone_mobile).apply()
                            preferences.edit().putString("birth_date", user.birthdate).apply()
                            preferences.edit().putString("email", user.email).apply()
                            preferences.edit().putString("password", password).apply()
                            preferences.edit().putString("role", user.role).apply()
                            preferences.edit().putInt("ak_id", user.ak_id).apply()
                            preferences.edit().putString("status", user.status).apply()
                            val intent = Intent(applicationContext, SplashScreenActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                PeersSnackbar.popUpSnack(mainView, "Username / Password Salah!")
                Log.e("LoginActivity", t.message.toString())
            }
        })

    }
}
