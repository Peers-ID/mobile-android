package com.android.id.peers.loans

import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.id.peers.R
import com.android.id.peers.loans.adapters.LoansAdapter
import com.android.id.peers.loans.models.LoanItem

class LoanDisbursementActivity : AppCompatActivity() {

    val loans: ArrayList<LoanItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_disbursement)

        val loanDisburseItem = LoanItem()
        loanDisburseItem.loanNo = "312190F"
        loanDisburseItem.memberName = "Aditya"
        loanDisburseItem.disburseAmount = 1250000

        val loanDisburseItem2 = LoanItem()
        loanDisburseItem2.loanNo = "239101D"
        loanDisburseItem2.memberName = "Ricky"
        loanDisburseItem2.disburseAmount = 2500000

        loans.add(loanDisburseItem)
        loans.add(loanDisburseItem2)
        loans.add(loanDisburseItem2)

        val loanDisbursement = findViewById<RecyclerView>(R.id.loan_disbursement)

        loanDisbursement.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        loanDisbursement.layoutManager = llm

        loanDisbursement.adapter = LoansAdapter(loans,this)

        loanDisbursement.addItemDecoration(
            DividerItemDecoration(this,
            DividerItemDecoration.HORIZONTAL)
        )
        loanDisbursement.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))

        loanDisbursement.setOnClickListener {
            /** A safe way to get an instance of the Camera object. */
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

}
