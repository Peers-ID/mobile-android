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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.auth.ChangePasswordActivity
import com.android.id.peers.loans.LoanApplicationActivity
import com.android.id.peers.loans.LoanDisbursementActivity
import com.android.id.peers.loans.RepaymentCollectionActivity
import com.android.id.peers.auth.LoginActivity
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.members.MemberAcquisitionActivity
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.NetworkConnectivity
import com.android.id.peers.util.database.OfflineDatabase
import com.android.id.peers.util.database.OfflineViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var offlineViewModel: OfflineViewModel
    private lateinit var mLoans: List<Loan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        offlineViewModel = ViewModelProvider(this).get(OfflineViewModel::class.java)
        offlineViewModel.allLoans.observe(this, Observer {loans ->
            mLoans = loans
            Log.d("MainActivity", "TEST")
            Log.d("MainActivity", "Size : ${mLoans.size}")
        })

        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setLogo(R.drawable.peers_logo_dark)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        title = ""

        val networkConnectivity = NetworkConnectivity(this.baseContext)
        val isConnected = networkConnectivity.isNetworkConnected()
        val temp = "Is connected? : $isConnected"
        Log.d("MainActivity", temp)


//        launch {
//            try {
//                val loanDao = OfflineDatabase.getDatabase(application).loanDao()
//                Log.d("MainActivity", "Size of Loan :")
//                val loans = loanDao.getAll()
//                Log.d("MainActivity", "Size of Loan : ${loans.size}")
//            } catch (exception: Exception) {
//
//            }
//        }


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

    private var job : Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}
