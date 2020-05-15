package com.android.id.peers.loans

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.lifecycle.Observer
import com.android.id.peers.R
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanFormulaConfig
import com.android.id.peers.loans.models.LoanFormulaConfig.Companion.saveLoanFormula
import com.android.id.peers.loans.models.OtherFees
import com.android.id.peers.loans.models.OtherFees.Companion.saveOtherFees
import com.android.id.peers.util.callback.LoanApplication
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_blank.*
import kotlinx.android.synthetic.main.activity_loan_application.*
import kotlin.properties.Delegates

class LoanApplicationActivity : AppCompatActivity() {
    val activity = this

    var done: Int by Delegates.observable(0) { property, oldValue, newValue ->
        if(newValue == 2) {
            val intent = Intent(activity, LoanApplicationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loanPreferences = getSharedPreferences("loan_config", Context.MODE_PRIVATE)
        val formulaId = loanPreferences.getInt("id", 0)
        if(formulaId == 0) {
            setContentView(R.layout.activity_blank)
            perbarui.setOnClickListener {
                val connectionStateMonitor = ConnectionStateMonitor(application)
                connectionStateMonitor.observe(this, Observer { isConnected ->
                    if (isConnected) {
                        val apiConnections = ApiConnections()
                        val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                        apiConnections.authenticate(preferences, this, ApiConnections.REQUEST_TYPE_GET_LOAN_FORMULA,
                            object :
                                LoanApplication {
                                override fun onSuccess(result: LoanFormulaConfig) {
                                    val loanPreferences = getSharedPreferences("loan_config", Context.MODE_PRIVATE)
                                    saveLoanFormula(loanPreferences, result)
                                    done += 1
                                }

                                override fun onSuccess(result: List<OtherFees>) {
                                    val feePreferences = getSharedPreferences("fee_config", Context.MODE_PRIVATE)
                                    saveOtherFees(feePreferences, result)
                                    done += 1
                                }
                            })
                    }
                })
            }
        } else {
            setContentView(R.layout.activity_loan_application)
            title = "Loan Application"

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels

            ajukan.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    ajukan.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                Log.d("TermsActivity", "HAHAHA" + String.format("%d", termsText.height))
                    val temp = height - ajukan.height - 7 * resources.getDimension(R.dimen.activity_vertical_margin).toInt()
                    scroll_view.layoutParams.height = temp
                    scroll_view.requestLayout()
//                Log.d("TermsActivity", "WKWKWK" + String.format("%d", termsTextArea.layoutParams.height))
                }
            })

//            val memberPreferences = getSharedPreferences("member", Context.MODE_PRIVATE)
            val feePreferences = getSharedPreferences("fee_config", Context.MODE_PRIVATE)
            if(intent.extras != null) {
                val noHp = intent.extras!!.getString("member_handphone")
                if(noHp != null) {
                    handphone_no.setText(noHp)
                }
            }

//        val memberName = intent.extras!!.getString("member_name")

//        val formulaId = loanPreferences.getInt("id", 0)
            Log.d("LoanApplication", "Formula Id : $formulaId")
            val tenureCycle = loanPreferences.getString("tenure_cycle", null)!!
            val serviceFeeAmount = loanPreferences.getLong("service_amount", 0)
            val serviceFeeCycle = loanPreferences.getString("service_cycle", null)!!

            val tenureMultiplier = if(tenureCycle == "bulan") 30 else if(tenureCycle == "minggu") 7 else 1
            val serviceFeeMultiplier = if(serviceFeeCycle == "bulan") 30 else if(serviceFeeCycle == "minggu") 7 else 1

            service_fee.text = String.format("%d", serviceFeeAmount)

            val feeJson = feePreferences.getString("fees", null)!!
            val otherFees = Gson().fromJson<List<OtherFees>>(feeJson)
            var sumFee: Long = 0

            val minLoan = loanPreferences.getInt("min_loan_amount", 0)
            val stepLoan = loanPreferences.getInt("kelipatan", 0)
            val maxLoan = loanPreferences.getInt("max_loan_amount", 0)
            min_loan_text.text = String.format("%d", minLoan)
            max_loan_text.text = String.format("%d", maxLoan)
            number_of_loan_seek_bar.progress = 0
            number_of_loan_seek_bar.max = maxLoan
            number_of_loan_seek_bar.progress = maxLoan/2
            number_of_loan_seek_bar.incrementProgressBy(stepLoan)
            number_of_loan.setText(String.format("%d", number_of_loan_seek_bar.progress))

            val minTenor = loanPreferences.getInt("min_tenure", 0)
            val stepTenor = 1
            val maxTenor = loanPreferences.getInt("max_tenure", 0)
            min_tenor_text.text = String.format("%d", minTenor)
            max_tenor_text.text = String.format("%d", maxTenor)
            tenor_seek_bar.progress = 0
            tenor_seek_bar.max = maxTenor
            tenor_seek_bar.progress = maxTenor/2
            tenor_seek_bar.incrementProgressBy(stepTenor)
            tenor.setText(String.format("%d", tenor_seek_bar.progress))

            for(f in otherFees) {
                val fMultiplier = if(f.serviceCycle == "bulan") 30 else if(f.serviceCycle == "minggu") 7 else 1
                sumFee += tenor_seek_bar.progress * tenureMultiplier / fMultiplier * f.serviceAmount
            }

            var loanDisbursement = number_of_loan.text.toString().toLong() - tenor.text.toString().toInt() * tenureMultiplier / serviceFeeMultiplier * serviceFeeAmount - sumFee
            Log.d("LoanApplication", "${tenor.text.toString().toInt()}")
            Log.d("LoanApplication", "$tenureMultiplier")
            var cicilanPerBulan = loanDisbursement / (tenor.text.toString().toInt() * tenureMultiplier)
            loan_disbursement.text = loanDisbursement.toString()
            cicilan.text = cicilanPerBulan.toString()
            number_of_loan_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    // Display the current progress of SeekBar
                    number_of_loan.setText(String.format("%d", i/stepLoan*stepLoan))
                    if (number_of_loan_seek_bar.progress < minLoan) {
                        number_of_loan_seek_bar.progress = minLoan
                        number_of_loan.setText(String.format("%d", minLoan))
                    }
                    loanDisbursement = number_of_loan.text.toString().toLong() - tenor.text.toString().toInt() * tenureMultiplier / serviceFeeMultiplier * serviceFeeAmount - sumFee
                    cicilanPerBulan = loanDisbursement / (tenor.text.toString().toInt() * tenureMultiplier)
                    loan_disbursement.text = loanDisbursement.toString()
                    cicilan.text= cicilanPerBulan.toString()
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


            tenor_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    // Display the current progress of SeekBar
                    tenor.setText(String.format("%d", i/stepTenor*stepTenor))
                    if (tenor_seek_bar.progress < minTenor) {
                        tenor_seek_bar.progress = minTenor
                        tenor.setText(String.format("%d", minTenor))
                    }
                    loanDisbursement = number_of_loan.text.toString().toLong() - tenor.text.toString().toInt() * tenureMultiplier / serviceFeeMultiplier * serviceFeeAmount - sumFee
                    cicilanPerBulan = loanDisbursement / (tenor.text.toString().toInt() * tenureMultiplier)
                    loan_disbursement.text = loanDisbursement.toString()
                    cicilan.text= cicilanPerBulan.toString()
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

            var index = 1

            for(feeItem in otherFees) {
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
                otherFeeText.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 1f)
                otherFeeText.id = TextView.generateViewId()
                otherFeeText.text = feeItem.serviceName

                val otherFee = TextView(this)
                otherFee.layoutParams = TableRow.LayoutParams(0, WRAP_CONTENT, 1f)
                otherFee.id = TextView.generateViewId()
                otherFee.text = feeItem.serviceAmount.toString()

                tableRow.addView(otherFeeText)
                tableRow.addView(otherFee)
                table_layout.addView(tableRow, index)
                index += 1
            }

            ajukan.setOnClickListener {
                var allTrue = true

                if(handphone_no.text.toString().isEmpty()) {
                    handphone_no_container.error = "Nomor HP tidak boleh kosong"
                    allTrue = false
                }
                if(allTrue) {
                    val intent = Intent(this, LoanApplicationConfirmationActivity::class.java)
                    val fees = ArrayList<Pair<String, Long>>()
                    for(fee in otherFees) {
                        fees.add(Pair(fee.serviceName, fee.serviceAmount))
                    }
                    val loan = Loan(otherFees = fees)
                    loan.noHp = handphone_no.text.toString()
                    loan.numberOfLoan = number_of_loan.text.toString().toLong()
                    loan.tenor = tenor.text.toString().toInt()
                    loan.formulaId = formulaId
                    val loginPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
                    loan.aoId = loginPreferences.getInt("id", 0)
                    loan.totalDisbursed = loanDisbursement
                    loan.cicilanPerBulan = cicilanPerBulan
                    loan.serviceFee = tenor.text.toString().toLong()
//                if(memberName != null) {
                    intent.putExtra("member_handphone", handphone_no.text.toString())
//                    intent.putExtra("member_name", memberName)
//                }
                    intent.putExtra("number_of_loan", loan)
                    startActivity(intent)
                }
            }
        }
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object: TypeToken<T>() {}.type)
}
