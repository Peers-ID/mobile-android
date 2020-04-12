package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.RcdTablePagerAdapter
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.callback.RepaymentCollection
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_repayment_collection_detail.*

class RepaymentCollectionDetailActivity : AppCompatActivity() {
    private var memberViewModel = MemberViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment_collection_detail)
        title = "Repayment Collection"

        memberViewModel = ViewModelProvider(this).get(MemberViewModel::class.java)

        val memberId = intent.extras!!.getInt("member_id")

        val apiConnections = ApiConnections()
        apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
            this, ApiConnections.REQUEST_TYPE_GET_MEMBER, object : RepaymentCollection {
                override fun onSuccess(result: Member) {
                    Log.d("RepaymentDetail", result.namaLengkap)
//                    memberDataListener!!.onReceived(result)
                    memberViewModel.setMember(result)
                }

            }, memberId)

        pager.adapter = RcdTablePagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(pager)

        simpan.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }
}
