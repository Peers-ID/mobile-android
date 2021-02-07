package com.android.id.peers.loans_unused

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.id.peers.R
import com.android.id.peers.VerificationActivity
import com.android.id.peers.loans_unused.models.Loan
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.CurrencyFormat
import com.android.id.peers.util.connection.ConnectionStateMonitor

import kotlinx.android.synthetic.main.activity_loan_application_confirmation.*

class LoanApplicationConfirmationActivity : AppCompatActivity() {

    var member: Member? = null
    var context = this

    var connected: Boolean = true

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application_confirmation)
        title = "Loan Application"

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

        if (intent.extras != null) {
            if (intent.getParcelableExtra<Member>("member") != null) {
                member = (intent.getParcelableExtra("member"))!!
            }
        }

        val loan = intent.getParcelableExtra<Loan>("number_of_loan")
//        val memberName = intent.getStringExtra("member_name")

        if(loan != null) {
//            member_name.text = loan.memberName
            handphone_no.text = loan.noHp
            number_of_loan.text = CurrencyFormat.formatRupiah.format(loan.numberOfLoan)
            tenor.text = String.format("%d", loan.tenor)
            service_fee.text = CurrencyFormat.formatRupiah.format(loan.serviceFee)
            pembayaran_cicilan.text = CurrencyFormat.formatRupiah.format(loan.totalDisbursed)
            cicilan.text = CurrencyFormat.formatRupiah.format(loan.cicilanPerBulan)

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

            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            connected = activeNetwork?.isConnectedOrConnecting == true

            if (connected) {
                Log.d("LoanApplication", "There is internet connection")
                val intent = Intent(this, VerificationActivity::class.java)
                if (member != null) {
                    intent.putExtra("member", member)
                }
                intent.putExtra("loan", loan)
                intent.putExtra("hand_phone", handphone_no.text.toString())
                startActivity(intent)
            } /*else {
                Log.d("LoanApplication", "There is no internet connection")
                PeersSnackbar.popUpSnack(context.window.decorView, "There was no internet access! Data is saved locally")
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val data = Loan.putLoanOnDataBuilder(loan!!)

//                val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(OfflineViewModel::class.java)
                if (member != null) {
                    val data2 = Member.putMemberOnDataBuilder(member!!)
//                    offlineViewModel.insertMember(member!!)
                    val memberWorker = OneTimeWorkRequestBuilder<MemberWorker>()
                        .setConstraints(constraints)
                        .setInputData(data2.build())
                        .build() // or PeriodicWorkRequest

                    val loanWorker = OneTimeWorkRequestBuilder<LoanWorker>()
                        .setConstraints(constraints)
                        .setInputData(data.build())
                        .build() // or PeriodicWorkRequest

                    WorkManager.getInstance(context)
                        .beginWith(memberWorker)
                        .then(loanWorker)
                        .enqueue()
                } else {
                    val loanWorker = OneTimeWorkRequestBuilder<LoanWorker>()
                        .setConstraints(constraints)
                        .setInputData(data.build())
                        .build() // or PeriodicWorkRequest

                    WorkManager.getInstance(context).enqueue(loanWorker)
                }
//                offlineViewModel.insertLoan(loan!!)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }*/
//            connectionStateMonitor.observe(this, Observer { isConnected ->
//
//            })
        }
    }


}
