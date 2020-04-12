package com.android.id.peers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.android.id.peers.auth.ChangePasswordActivity
import com.android.id.peers.loans.LoanApplicationActivity
import com.android.id.peers.loans.LoanDisbursementActivity
import com.android.id.peers.loans.RepaymentCollectionActivity
import com.android.id.peers.auth.LoginActivity
import com.android.id.peers.members.MemberAcquisitionActivity
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.NetworkConnectivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setLogo(R.drawable.peers_logo_dark)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        title = ""

        val networkConnectivity = NetworkConnectivity(this.baseContext)
        val isConnected = networkConnectivity.isNetworkConnected()
        val temp = "Is connected? : $isConnected"
        Log.d("MainActivity", temp)

        val memberAcquisition = findViewById<CardView>(R.id.member_acquisition)
        val loanApplication = findViewById<CardView>(R.id.loan_application)
        val loanDisbursement = findViewById<CardView>(R.id.loan_disbursement)
        val repaymentCollection = findViewById<CardView>(R.id.repayment_collection)
        memberAcquisition.setOnClickListener { memberAcquisition(it) }
        loanApplication.setOnClickListener { loanApplication() }
        loanDisbursement.setOnClickListener { loanDisbursement() }
        repaymentCollection.setOnClickListener { repaymentCollection() }
    }

    private fun memberAcquisition(view: View) {
        val intent = Intent(this, MemberAcquisitionActivity::class.java)
        startActivity(intent)
    }

    private fun loanApplication() {
        val intent = Intent(this, LoanApplicationActivity::class.java)
        startActivity(intent)
    }

    private fun loanDisbursement() {
        val intent = Intent(this, LoanDisbursementActivity::class.java)
        startActivity(intent)
    }

    private fun repaymentCollection() {
        val intent = Intent(this, RepaymentCollectionActivity::class.java)
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
                val intent = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                preferences.edit().clear().apply()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            }
            else -> {
//                Toast.makeText(this, item.itemId, Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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
