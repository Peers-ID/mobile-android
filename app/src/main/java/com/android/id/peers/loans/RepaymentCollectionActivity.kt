package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.LoansAdapter
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanItem
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_repayment_collection.*

class RepaymentCollectionActivity : AppCompatActivity() {
    var activity = this
    var memberId: Int = 0

//    val loans: ArrayList<LoanItem> = ArrayList()
//    var loan: List<Loan> = ArrayList()
//    var loans: ArrayList<Loan> = ArrayList()
    val loans: ArrayList<Loan> = ArrayList()
    //    var loan: ArrayList<Loan> = ArrayList()
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
                    loan = ArrayList(result)
                    for (l in loan) {
                        val loanDisburseItem = Loan(otherFees = ArrayList())
                        memberId = l.memberId
//                        loanDisburseItem.loanNo = ""
                        loanDisburseItem.memberId = l.memberId
                        loanDisburseItem.memberName = l.memberName
                        loanDisburseItem.cicilanPerBulan = l.cicilanPerBulan
                        loanDisburseItem.totalDisbursed = l.totalDisbursed
                        loans.add(loanDisburseItem)
                    }
//                    Log.d("RepaymentCollection", "Cicilan : ${loans[0].cicilanPerBulan}")
                    loan_disbursement.adapter!!.notifyDataSetChanged()
                }
            })

        loan_disbursement.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        loan_disbursement.layoutManager = llm

//        loanDisbursement.adapter = LoansAdapter(loans,this, 1) { loan : LoanItem -> loanItemClicked(loan)}
//        loan_disbursement.adapter = LoansAdapter(loans, activity, 1) { loan : LoanItem -> loanItemClicked(loan)}
        loan_disbursement.adapter = LoansAdapter(loans, activity, 1) { loan : Loan -> loanItemClicked(loan)}

        loan_disbursement.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL)
        )
        loan_disbursement.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )

    }

//    private fun loanItemClicked(loan : LoanItem) {
    private fun loanItemClicked(loan : Loan) {
        val intent = Intent(this, RepaymentCollectionDetailActivity::class.java)
        val loginPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        intent.putExtra("koperasi_id", loginPreferences.getInt("koperasi_id", 0))
        intent.putExtra("member_id", loan.memberId)
        intent.putExtra("ao_id", loan.aoId)
        intent.putExtra("loan_id", loan.id)
        intent.putExtra("loan_disbursed", loan.totalDisbursed)
        intent.putExtra("loan_cicilan", loan.cicilanPerBulan)
        startActivity(intent)
    }
}
