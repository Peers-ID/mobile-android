package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.LoansAdapter
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_repayment_collection.*

class RepaymentCollectionActivity : AppCompatActivity() {
    var activity = this
//    var memberId: Int = 0

//    val loans: ArrayList<LoanItem> = ArrayList()
//    var loan: List<Loan> = ArrayList()
//    var loans: ArrayList<Loan> = ArrayList()
    var loans: ArrayList<Loan> = ArrayList()
    //    var loan: ArrayList<Loan> = ArrayList()
    lateinit var loan: List<Loan>

    val loansAdapter = LoansAdapter(loans, activity, 1) { loan : Loan -> loanItemClicked(loan)}

    var connected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment_collection)
        title = "Repayment Collection"

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        loan_disbursement.adapter = loansAdapter

        val loanPreferences = getSharedPreferences("loans", Context.MODE_PRIVATE)

        if (connected) {
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmerAnimation()
            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
                    LoanDisbursement {
                    override fun onSuccess(result: List<Loan>) {
                        loan = ArrayList(result)
                        for (l in loan) {
                            val loanDisburseItem = Loan(otherFees = ArrayList())
//                            memberId = l.memberId
//                        loanDisburseItem.loanNo = ""
                            loanDisburseItem.tenor = l.tenor
                            loanDisburseItem.id = l.id
                            loanDisburseItem.memberId = l.memberId
                            loanDisburseItem.memberName = l.memberName
                            loanDisburseItem.cicilanPerBulan = l.cicilanPerBulan
                            loanDisburseItem.cicilanKe = l.cicilanKe
                            loanDisburseItem.aoId = l.aoId
                            loanDisburseItem.totalDisbursed = l.totalDisbursed

                            loans.add(loanDisburseItem)
                        }
//                    Log.d("RepaymentCollection", "Cicilan : ${loans[0].cicilanPerBulan}")

                        Loan.saveLoans(loanPreferences, result, "repayment_collection")

                        loan_disbursement.adapter!!.notifyDataSetChanged()
                        shimmer_view_container.stopShimmerAnimation()
                        shimmer_view_container.visibility = View.GONE
                    }
                }, listType = 1)
        } else {
            val loanJson = loanPreferences.getString("repayment_collection", null)!!
            loans = Gson().fromJson(loanJson)
            loansAdapter.notifyDataSetChanged()
        }

        loan_disbursement.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        loan_disbursement.layoutManager = llm

//        loanDisbursement.adapter = LoansAdapter(loans,this, 1) { loan : LoanItem -> loanItemClicked(loan)}
//        loan_disbursement.adapter = LoansAdapter(loans, activity, 1) { loan : LoanItem -> loanItemClicked(loan)}

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
        intent.putExtra("loan", loan)
        intent.putExtra("koperasi_id", loginPreferences.getInt("koperasi_id", 0))
        intent.putExtra("member_id", loan.memberId)
        intent.putExtra("ao_id", loginPreferences.getInt("id", 0))
        intent.putExtra("loan_id", loan.id)
//        intent.putExtra("loan_disbursed", loan.totalDisbursed)
        intent.putExtra("loan_cicilan", loan.cicilanPerBulan)
        intent.putExtra("cicilan_ke", loan.cicilanKe)
        startActivity(intent)
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object: TypeToken<T>() {}.type)
}
