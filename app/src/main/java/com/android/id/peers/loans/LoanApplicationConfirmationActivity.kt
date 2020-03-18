package com.android.id.peers.loans

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.android.id.peers.R
import com.android.id.peers.loans.model.Loan
import com.android.id.peers.members.TermsActivity
import kotlinx.android.synthetic.main.activity_loan_application.*

class LoanApplicationConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application_confirmation)
        title = "Loan Application"

        val loan = intent.getParcelableExtra<Loan>("numberOfLoan")

        val handphoneNo = findViewById<TextView>(R.id.emergency_handphone_no)
        val numberOfLoan = findViewById<TextView>(R.id.number_of_loan)
        val tenor = findViewById<TextView>(R.id.tenor)
        val serviceFee = findViewById<TextView>(R.id.service_fee)
        val lanjutkan = findViewById<Button>(R.id.lanjutkan)

        if(loan != null) {
            handphoneNo.text = loan.noHp
            numberOfLoan.text = String.format("%d", loan.numberOfLoan)
            tenor.text = String.format("%d", loan.tenor)
            serviceFee.text = String.format("%d", loan.serviceFee)

            for(fee in loan.otherFees) {
                val linearLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayoutParams.marginStart = (resources. getDimension(R.dimen.activity_horizontal_margin)).toInt()
                linearLayoutParams.marginEnd = (resources. getDimension(R.dimen.activity_horizontal_margin)).toInt()
                val topMargin = R.dimen.margin_between
                linearLayoutParams.topMargin = (resources. getDimension(topMargin)).toInt()
                var bottomMargin = R.dimen.margin_between
                if(fee == loan.otherFees.last()) {
                    bottomMargin = R.dimen.activity_vertical_margin
                }
                linearLayoutParams.bottomMargin = (resources. getDimension(bottomMargin)).toInt()
                val feeContainer1 = ConstraintLayout(this)
                feeContainer1.id = ConstraintLayout.generateViewId()
                feeContainer1.layoutParams = linearLayoutParams
                this.loan_container.addView(feeContainer1)
                val otherFeeText = TextView(this)
                otherFeeText.id = TextView.generateViewId()
                otherFeeText.text = fee.first

                val otherFee = TextView(this)
                otherFee.id = TextView.generateViewId()
                otherFee.text = String.format("%d", fee.second)

                feeContainer1.addView(otherFeeText)
                feeContainer1.addView(otherFee)

                val constraintSet = ConstraintSet()
                constraintSet.clone(feeContainer1)
                constraintSet.constrainHeight(otherFeeText.id, ConstraintSet.WRAP_CONTENT)
                constraintSet.constrainWidth(otherFeeText.id, ConstraintSet.WRAP_CONTENT)
                constraintSet.setHorizontalBias(otherFeeText.id, 0F)

                constraintSet.constrainHeight(otherFee.id, ConstraintSet.WRAP_CONTENT)
                constraintSet.constrainWidth(otherFee.id, ConstraintSet.WRAP_CONTENT)
                constraintSet.setHorizontalBias(otherFee.id, 0.5F)

                constraintSet.connect(otherFeeText.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(otherFeeText.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                constraintSet.connect(otherFeeText.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                constraintSet.connect(otherFeeText.id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )

                constraintSet.connect(otherFee.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(otherFee.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                constraintSet.connect(otherFee.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                constraintSet.connect(otherFee.id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )

                constraintSet.applyTo(feeContainer1)
            }
        }

        lanjutkan.setOnClickListener {
            val intent = Intent(this, TermsActivity::class.java)
            startActivity(intent)
        }
    }
}
