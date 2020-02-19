package id.peers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import id.peers.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)

        preferences.edit().remove("username").apply()

        val username = preferences.getString("username", "")

        if (username != null) {
            if(username.isEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        findViewById<ImageView>(R.id.member_acquisition_icon).setOnClickListener { memberAcquisition(it) }
    }

    private fun memberAcquisition(view: View) {
        val intent = Intent(this, MemberAcquisitionActivity::class.java)
        startActivity(intent)
    }
}
