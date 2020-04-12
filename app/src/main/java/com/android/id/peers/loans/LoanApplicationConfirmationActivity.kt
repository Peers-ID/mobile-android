package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.android.id.peers.R
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.TermsActivity
import com.android.id.peers.VerificationActivity
import com.android.id.peers.util.connection.ApiConnections
import kotlinx.android.synthetic.main.activity_loan_application_confirmation.*

class LoanApplicationConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application_confirmation)
        title = "Loan Application"

        val loan = intent.getParcelableExtra<Loan>("number_of_loan")
        val noHp = intent.extras!!.getString("member_handphone")
        val memberName = intent.getStringExtra("member_name")

        if(loan != null) {
            handphone_no.text = loan.noHp
            number_of_loan.text = String.format("%d", loan.numberOfLoan)
            tenor.text = String.format("%d", loan.tenor)
            service_fee.text = String.format("%d", loan.serviceFee)
            loan_disbursement.text = loan.totalDisbursed.toString()
            cicilan.text = loan.cicilanPerBulan.toString()

            var index = 4

            for(fee in loan.otherFees) {
                val tableRowParams = TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
                )
                tableRowParams.marginStart = (resources.getDimension(R.dimen.activity_horizontal_margin)).toInt()
                tableRowParams.marginEnd = (resources.getDimension(R.dimen.activity_horizontal_margin)).toInt()
                tableRowParams.topMargin = (resources.getDimension(R.dimen.margin_between)).toInt()
                tableRowParams.bottomMargin = (resources.getDimension(R.dimen.margin_between)).toInt()
                val tableRow = TableRow(this)
                tableRow.layoutParams = tableRowParams
                val otherFeeText = TextView(this)
                otherFeeText.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                otherFeeText.id = TextView.generateViewId()
                otherFeeText.text = fee.first

                val otherFee = TextView(this)
                otherFee.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                otherFee.id = TextView.generateViewId()
                otherFee.text = fee.second.toString()

                tableRow.addView(otherFeeText)
                tableRow.addView(otherFee)
                loan_container.addView(tableRow, index)
                index += 1
            }
        }

        lanjutkan.setOnClickListener {
            val intent = Intent(this, VerificationActivity::class.java)
            val apiConnections = ApiConnections()
            apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                this, ApiConnections.REQUEST_TYPE_POST_LOAN, loan)
            intent.putExtra("hand_phone", handphone_no.text.toString())
            startActivity(intent)
        }
    }
}
