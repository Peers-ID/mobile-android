package com.android.id.peers.loans

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.RcdTablePagerAdapter
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.CurrencyFormat
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.callback.RepaymentCollection
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.database.OfflineViewModel
import com.android.id.peers.util.workers.CollectionWorker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_repayment_collection_detail.*

class RepaymentCollectionDetailActivity : AppCompatActivity() {
    private var memberViewModel = MemberViewModel()
    private var collection = com.android.id.peers.loans.models.Collection()
    private lateinit var loan: Loan

    var connected: Boolean = true

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment_collection_detail)
        title = "Repayment Collection"

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

        memberViewModel = ViewModelProvider(this).get(MemberViewModel::class.java)

        collection.koperasiId = intent.getIntExtra("koperasi_id", 0)
        collection.memberId = intent.getIntExtra("member_id", 0)
        collection.aoId = intent.getIntExtra("ao_id", 0)
        collection.loanId = intent.getIntExtra("loan_id", 0)
//        val loanDisbursed = intent.getLongExtra("loan_disbursed", 0)
        collection.cicilanJumlah = intent.getLongExtra("loan_cicilan", 0)
        collection.cicilanKe = intent.getIntExtra("cicilan_ke", 0)

        loan = intent.getParcelableExtra("loan")!!

        cicilan.setText(CurrencyFormat.formatAmount(collection.cicilanJumlah.toString()))

        var textBefore = ""
        pokok.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                // 1. get cursor position : p0 = start + before
                val initialCursorPosition = start + before
                //2. get digit count after cursor position : c0
                val numOfDigitsToRightOfCursor = CurrencyFormat.getNumberOfDigits(textBefore.substring(initialCursorPosition,
                    textBefore.length))

                val newAmount = CurrencyFormat.formatAmount(s.toString())
                pokok.removeTextChangedListener(this)
                pokok.setText(newAmount)
                //set new cursor position
                pokok.setSelection(CurrencyFormat.getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                pokok.addTextChangedListener(this)
            }

        })

        var textBefore2 = ""
        sukarela.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore2 = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                // 1. get cursor position : p0 = start + before
                val initialCursorPosition = start + before
                //2. get digit count after cursor position : c0
                val numOfDigitsToRightOfCursor = CurrencyFormat.getNumberOfDigits(textBefore2.substring(initialCursorPosition,
                    textBefore2.length))

                val newAmount = CurrencyFormat.formatAmount(s.toString())
                sukarela.removeTextChangedListener(this)
                sukarela.setText(newAmount)
                //set new cursor position
                sukarela.setSelection(CurrencyFormat.getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                sukarela.addTextChangedListener(this)
            }

        })
//        pokok.setText(loanDisbursed.toString())

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        if (connected) {
            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                this, ApiConnections.REQUEST_TYPE_GET_MEMBER, object : RepaymentCollection {
                    override fun onSuccess(result: Member) {
                        Log.d("RepaymentDetail", result.namaLengkap)
//                    memberDataListener!!.onReceived(result)
                        memberViewModel.setMember(result)
                    }

                }, collection.memberId)
        } else {
            PeersSnackbar.popUpSnack(this.window.decorView, "There is no internet connection!")
        }

        pager.adapter = RcdTablePagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(pager)

        simpan.setOnClickListener{

            var allTrue = true

            if(pokok.text.toString().isEmpty()) {
                pokok_container.error = "Simpanan pokok tidak boleh kosong"
                allTrue = false
            }
            if(sukarela.text.toString().isEmpty()) {
                sukarela_container.error = "Simpanan sukarela tidak boleh kosong"
                allTrue = false
            }

            if(allTrue) {
                collection.pokok = CurrencyFormat.removeCurrencyFormat(pokok.text.toString()).toLong()
                collection.sukarela = CurrencyFormat.removeCurrencyFormat(sukarela.text.toString()).toLong()

                connected = activeNetwork?.isConnectedOrConnecting == true

                val intent = Intent(this, MainActivity::class.java)

                if (connected) {
                    authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                        this, ApiConnections.REQUEST_TYPE_POST_COLLECTION, collection)
                    intent.putExtra("message", "Collection data has been successfully submitted")
                } else {
//                    val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
//                        OfflineViewModel::class.java)
//                    offlineViewModel.insertCollection(collection)
                    val loanPreferences = getSharedPreferences("loans", Context.MODE_PRIVATE)
                    val loanJson = loanPreferences.getString("repayment_collection", null)!!
                    val loans = Gson().fromJson<List<Loan>>(loanJson)
                    val l = ArrayList<Loan>(loans)
                    l.remove(loan)
                    Loan.saveLoans(loanPreferences, l, "repayment_collection")

                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val data = Data.Builder()
                    data.put("koperasi_id", collection.koperasiId)
                    data.put("member_id", collection.memberId)
                    data.put("ao_id", collection.aoId)
                    data.put("loan_id", collection.loanId)
                    data.put("loan_cicilan", collection.cicilanJumlah)
                    data.put("cicilan_ke", collection.cicilanKe)
                    val collectionWorker = OneTimeWorkRequestBuilder<CollectionWorker>()
                        .setConstraints(constraints)
                        .setInputData(data.build())
                        .build() // or PeriodicWorkRequest

                    WorkManager.getInstance(this).enqueue(collectionWorker)

                    intent.putExtra("message", "There was no internet access! Data is saved locally")
                }

                finishAffinity()
                startActivity(intent)
            }
        }
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object: TypeToken<T>() {}.type)
}
