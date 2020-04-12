package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.LoansAdapter
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanItem
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_repayment_collection.*

class RepaymentCollectionActivity : AppCompatActivity() {
    var activity = this
    var memberId: Int = 0

    val loans: ArrayList<LoanItem> = ArrayList()
    lateinit var loan: List<Loan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment_collection)
        title = "Repayment Collection"

        val apiConnections = ApiConnections()
        apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
            this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
                LoanDisbursement {
                override fun onSuccess(result: List<Loan>) {
                    loan = ArrayList<Loan>(result)
                    for (l in loan) {
                        val loanDisburseItem = LoanItem()
                        memberId = l.memberId
                        loanDisburseItem.loanNo = ""
                        loanDisburseItem.memberName = l.memberName
//                        loanDisburseItem.cicilan = l.cicilanPerBulan
                        loanDisburseItem.disburseAmount = l.totalDisbursed
                        loans.add(loanDisburseItem)
                    }
                    loan_disbursement.adapter!!.notifyDataSetChanged()
                }
            })

        loan_disbursement.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        loan_disbursement.layoutManager = llm

//        loanDisbursement.adapter = LoansAdapter(loans,this, 1) { loan : LoanItem -> loanItemClicked(loan)}
        loan_disbursement.adapter = LoansAdapter(loans, activity, 1) { loan : LoanItem -> loanItemClicked(loan)}

        loan_disbursement.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL)
        )
        loan_disbursement.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )

    }

    private fun loanItemClicked(loan : LoanItem) {
        val intent = Intent(this, RepaymentCollectionDetailActivity::class.java)
        intent.putExtra("member_id", memberId)
        startActivity(intent)
    }
}
