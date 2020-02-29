package com.android.id.peers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.id.peers.login.LoginActivity
import com.android.id.peers.members.MemberAcquisitionActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)

        val username = preferences.getString("username", "")

        if (username != null) {
            if(username.isEmpty()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val memberAcquisition = findViewById<ImageView>(R.id.member_acquisition_icon)
        memberAcquisition.setOnClickListener { memberAcquisition(it) }
    }

    private fun memberAcquisition(view: View) {
        val intent = Intent(this, MemberAcquisitionActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_password -> {
//                val intent = Intent(this, ChangePasswordActivity::class.java)
//                startActivity(intent)
//                finish()
                Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show()
            }
            R.id.logout -> {
                val preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE)
                preferences.edit().remove("username").apply()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, item.itemId, Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
