package com.android.id.peers.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_change_password.*
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_change_password)

        submit.setOnClickListener { submit(it) }
    }

    fun submit(view: View) {
        var allTrue = true
        if (password.text.toString().isEmpty()) {
            password_container.error = "Password tidak boleh kosong"
            allTrue = false
        }
        if (new_password.text.toString().isEmpty()) {
            new_password_container.error = "Password tidak boleh kosong"
            allTrue = false
        }
        if (confirm_password.text.toString().isEmpty()) {
            confirm_password_container.error = "Password tidak boleh kosong"
            allTrue = false
        }
        if (confirm_password.text.toString() != new_password.text.toString()) {
            confirm_password_container.error = "Tidak boleh berbeda dengan new password"
            allTrue = false
        }
        if (allTrue) {
            changePassword(view, password.text.toString(), confirm_password.text.toString())
        }
    }

    val API_HOSTNAME = "http://13.212.188.255/api/v1/"

    private fun changePassword(view: View, password: String, newPassword: String) {
        val url = "${API_HOSTNAME}users/change_password"

        val params = HashMap<String, String>()
        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        params["email"] = preferences.getString("email", null)!!
        params["password"] = password
        params["password_new"] = newPassword
        params["token"] = preferences.getString("token", null)!!

        val parameters = JSONObject(params as Map<*, *>);
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, parameters,
            Response.Listener { response ->
                Log.d("ChangePasswordActivity", response.toString())
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val responseStatus = jsonObj.getString("status")
//                Log.d("LoginActivity", strResp)
                if (responseStatus.toInt() == 400 || responseStatus.toInt() == 401) {
                    popUpSnack(view, "Failed to change password!")
                } else if (responseStatus.toInt() == 201) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("message", "Password has been successfully changed")
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
}
