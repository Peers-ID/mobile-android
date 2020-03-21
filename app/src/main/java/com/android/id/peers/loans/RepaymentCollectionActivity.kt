package com.android.id.peers.loans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.id.peers.R
import com.android.id.peers.loans.models.LoanItem

class RepaymentCollectionActivity : AppCompatActivity() {

    val loans: ArrayList<LoanItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment_collection)
    }
}
