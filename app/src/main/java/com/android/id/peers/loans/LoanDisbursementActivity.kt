package com.android.id.peers.loans

import android.content.Context
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.LoansAdapter
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanItem
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_loan_disbursement.*

class LoanDisbursementActivity : AppCompatActivity() {

    val loans: ArrayList<LoanItem> = ArrayList()
    lateinit var loan: List<Loan>
    val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_disbursement)

        val apiConnections = ApiConnections()
        apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
    this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
                LoanDisbursement {
                override fun onSuccess(result: List<Loan>) {
                    loan = ArrayList<Loan>(result)
                    for (l in loan) {
                        val loanDisburseItem = LoanItem()
                        loanDisburseItem.loanNo = ""
                        loanDisburseItem.memberName = l.memberName
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

        loan_disbursement.adapter = LoansAdapter(loans, activity, 0) { loan : LoanItem -> loanItemClicked(loan)}

        loan_disbursement.addItemDecoration(
            DividerItemDecoration(this,
            DividerItemDecoration.HORIZONTAL)
        )
        loan_disbursement.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))

        loan_disbursement.setOnClickListener {
            /** A safe way to get an instance of the Camera object. */


        }
    }

    private fun loanItemClicked(loan : LoanItem) {
        Toast.makeText(this, "Clicked: ${loan.memberName}", Toast.LENGTH_LONG).show()
//        val intent = Intent(this, RepaymentCollectionDetailActivity::class.java)
//        startActivity(intent)
        fun getCameraInstance(): Camera? {
            return try {
                Camera.open() // attempt to get a Camera instance
            } catch (e: Exception) {
                // Camera is not available (in use or does not exist)
                null // returns null if camera is unavailable
            }
        }
    }
}
