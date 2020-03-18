package com.android.id.peers.loans

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.android.id.peers.R
import com.android.id.peers.loans.model.Loan
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_loan_application.*

class LoanApplicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application)
        title = "Loan Application"

        val minLoanText = findViewById<TextView>(R.id.min_loan_text)
        val maxLoanText = findViewById<TextView>(R.id.max_loan_text)
        val handphoneNoC = findViewById<TextInputLayout>(R.id.handphone_no_container)
        val handphoneNo = findViewById<TextInputEditText>(R.id.emergency_handphone_no)
        val numberOfLoanText = findViewById<TextInputEditText>(R.id.number_of_loan)
        val numberOfLoan = findViewById<SeekBar>(R.id.number_of_loan_seek_bar)
        var minLoan = 500000
        var stepLoan = 100000
        var maxLoan = 5000000
        minLoanText.text = String.format("%d", minLoan)
        maxLoanText.text = String.format("%d", maxLoan)
        numberOfLoan.progress = 0
        numberOfLoan.max = maxLoan
        numberOfLoan.progress = maxLoan/2
        numberOfLoan.incrementProgressBy(stepLoan)
        Toast.makeText(this, String.format("%d", numberOfLoan.progress), Toast.LENGTH_SHORT).show()
        numberOfLoanText.setText(String.format("%d", numberOfLoan.progress))
        numberOfLoan.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                numberOfLoanText.setText(String.format("%d", i/stepLoan*stepLoan))
                if (numberOfLoan.progress < minLoan) {
                    numberOfLoan.progress = minLoan
                    numberOfLoanText.setText(String.format("%d", minLoan))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
//                Toast.makeText(applicationContext, "start tracking", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
//                Toast.makeText(applicationContext, "stop tracking", Toast.LENGTH_SHORT).show()
            }
        })

        val minTenorText = findViewById<TextView>(R.id.min_tenor_text)
        val maxTenorText = findViewById<TextView>(R.id.max_tenor_text)
        val tenorText = findViewById<TextInputEditText>(R.id.tenor)
        val tenor = findViewById<SeekBar>(R.id.tenor_seek_bar)
        var minTenor = 200000
        var stepTenor = 200000
        var maxTenor = 2000000
        minTenorText.text = String.format("%d", minTenor)
        maxTenorText.text = String.format("%d", maxTenor)
        tenor.progress = 0
        tenor.max = maxTenor
        tenor.progress = maxTenor/2
        tenor.incrementProgressBy(stepTenor)
        Toast.makeText(this, String.format("%d", tenor.progress), Toast.LENGTH_SHORT).show()
        tenorText.setText(String.format("%d", tenor.progress))
        tenor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                tenorText.setText(String.format("%d", i/stepTenor*stepTenor))
                if (tenor.progress < minTenor) {
                    tenor.progress = minTenor
                    tenorText.setText(String.format("%d", minTenor))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
//                Toast.makeText(applicationContext, "start tracking", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
//                Toast.makeText(applicationContext, "stop tracking", Toast.LENGTH_SHORT).show()
            }
        })

        var fee = 100000
        var serviceFee = findViewById<TextView>(R.id.service_fee)
        serviceFee.text = String.format("%d", fee)

        for(i in 1..5) {
            val linearLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            linearLayoutParams.marginStart = (resources. getDimension(R.dimen.activity_horizontal_margin)).toInt()
            linearLayoutParams.marginEnd = (resources. getDimension(R.dimen.activity_horizontal_margin)).toInt()
            linearLayoutParams.topMargin = (resources. getDimension(R.dimen.activity_vertical_margin)).toInt()
            linearLayoutParams.bottomMargin = (resources. getDimension(R.dimen.margin_between)).toInt()
            val feeContainer1 = ConstraintLayout(this)
            feeContainer1.id = ConstraintLayout.generateViewId()
            feeContainer1.layoutParams = linearLayoutParams
            this.loan_container.addView(feeContainer1)
            val otherFeeText = TextView(this)
            otherFeeText.id = TextView.generateViewId()
            otherFeeText.text = "Other Fee"

            val otherFee = TextView(this)
            otherFee.id = TextView.generateViewId()
            otherFee.text = "2000000"

            feeContainer1.addView(otherFeeText)
            feeContainer1.addView(otherFee)

            val constraintSet = ConstraintSet()
            constraintSet.clone(feeContainer1)
            constraintSet.constrainHeight(otherFeeText.id, WRAP_CONTENT)
            constraintSet.constrainWidth(otherFeeText.id, WRAP_CONTENT)
            constraintSet.setHorizontalBias(otherFeeText.id, 0F)

            constraintSet.constrainHeight(otherFee.id, WRAP_CONTENT)
            constraintSet.constrainWidth(otherFee.id, WRAP_CONTENT)
            constraintSet.setHorizontalBias(otherFee.id, 0.5F)

            constraintSet.connect(otherFeeText.id, TOP, PARENT_ID, TOP)
            constraintSet.connect(otherFeeText.id, START, PARENT_ID, START)
            constraintSet.connect(otherFeeText.id, BOTTOM, PARENT_ID, BOTTOM)
            constraintSet.connect(otherFeeText.id, END, PARENT_ID, END)

            constraintSet.connect(otherFee.id, TOP, PARENT_ID, TOP)
            constraintSet.connect(otherFee.id, START, PARENT_ID, START)
            constraintSet.connect(otherFee.id, BOTTOM, PARENT_ID, BOTTOM)
            constraintSet.connect(otherFee.id, END, PARENT_ID, END)

            constraintSet.applyTo(feeContainer1)
        }

//        this.loan_container.addView(feeContainer1)
        val ajukan = findViewById<Button>(R.id.ajukan)
        ajukan.setOnClickListener {
            var allTrue = true

            if(handphoneNo.text.toString().isEmpty()) {
                handphoneNoC.error = "Nomor HP tidak boleh kosong"
                allTrue = false
            }
            if(allTrue) {
                val intent = Intent(this, LoanApplicationConfirmationActivity::class.java)
                val otherFees = ArrayList<Pair<String, Long>>()
                otherFees.add(Pair("Unexpected Fees", 2500000))
                otherFees.add(Pair("Additional Fees", 2500000))
                otherFees.add(Pair("Other Fees", 5000000))
                val loan = Loan(otherFees = otherFees)
                loan.noHp = handphoneNo.text.toString()
                loan.numberOfLoan = numberOfLoanText.text.toString().toLong()
                loan.tenor = tenorText.text.toString().toLong()
                loan.serviceFee = tenorText.text.toString().toLong()
                intent.putExtra("numberOfLoan", loan)
                startActivity(intent)
            }
        }

    }
}
