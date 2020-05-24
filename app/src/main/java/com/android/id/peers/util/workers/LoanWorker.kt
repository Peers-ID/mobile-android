package com.android.id.peers.util.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.util.callback.LoanApplicationCallback
import com.android.id.peers.util.connection.ApiConnections

class LoanWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    val context = appContext

    override fun doWork(): Result {

        val preferences = context.getSharedPreferences("login_data", Context.MODE_PRIVATE)
//        val preferences = inputData.keyValueMap["preferences"] as SharedPreferences

        val loan = Loan()
        loan.noHp = inputData.getString("no_hp")!!
        loan.numberOfLoan = inputData.getLong("number_of_loan", 0)
        loan.tenor = inputData.getInt("tenor", 0)
        loan.formulaId = inputData.getInt("formula_id", 0)
        loan.aoId = inputData.getInt("ao_id", 0)
        loan.totalDisbursed = inputData.getLong("total_disbursed", 0)
        loan.cicilanPerBulan = inputData.getLong("cicilan_per_bln", 0)
        loan.serviceFee = inputData.getLong("service_fee", 0)

        ApiConnections.authenticate(
            preferences,
            context, ApiConnections.REQUEST_TYPE_POST_LOAN, object :
                LoanApplicationCallback {
                override fun onSuccess(result: Boolean) {

                }

            }, loan = loan
        )

        return Result.success()
    }
}