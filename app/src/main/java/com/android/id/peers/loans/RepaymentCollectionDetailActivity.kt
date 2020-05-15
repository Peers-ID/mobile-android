package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.RcdTablePagerAdapter
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.callback.RepaymentCollection
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.database.OfflineViewModel
import kotlinx.android.synthetic.main.activity_repayment_collection_detail.*

class RepaymentCollectionDetailActivity : AppCompatActivity() {
    private var memberViewModel = MemberViewModel()
    private var collection = com.android.id.peers.loans.models.RepaymentCollection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment_collection_detail)
        title = "Repayment Collection"

        memberViewModel = ViewModelProvider(this).get(MemberViewModel::class.java)

        collection.koperasiId = intent.getIntExtra("koperasi_id", 0)
        collection.memberId = intent.getIntExtra("member_id", 0)
        collection.aoId = intent.getIntExtra("ao_id", 0)
        collection.loanId = intent.getIntExtra("loan_id", 0)
        val loanDisbursed = intent.getLongExtra("loan_disbursed", 0)
        collection.cicilanJumlah = intent.getLongExtra("loan_cicilan", 0)

        cicilan.setText(collection.cicilanJumlah.toString())
//        pokok.setText(loanDisbursed.toString())

        val apiConnections = ApiConnections()
        apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
            this, ApiConnections.REQUEST_TYPE_GET_MEMBER, object : RepaymentCollection {
                override fun onSuccess(result: Member) {
                    Log.d("RepaymentDetail", result.namaLengkap)
//                    memberDataListener!!.onReceived(result)
                    memberViewModel.setMember(result)
                }

            }, collection.memberId)

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
                collection.pokok = pokok.text.toString().toLong()
                collection.sukarela = sukarela.text.toString().toLong()

                val connectionStateMonitor = ConnectionStateMonitor(application)
                connectionStateMonitor.observe(this, Observer { isConnected ->
                    if (isConnected) {
                        apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                            this, ApiConnections.REQUEST_TYPE_POST_COLLECTION, collection)
                    } else {
                        val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
                            OfflineViewModel::class.java)
                        offlineViewModel.insertCollection(collection)
                    }
                })
                val intent = Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
        }
    }
}
