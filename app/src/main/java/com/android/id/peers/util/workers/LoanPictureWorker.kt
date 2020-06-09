package com.android.id.peers.util.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.id.peers.util.connection.ApiConnections

class LoanPictureWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    val context = appContext

    override fun doWork(): Result {

        val preferences = context.getSharedPreferences("login_data", Context.MODE_PRIVATE)
//        val preferences = inputData.keyValueMap["preferences"] as SharedPreferences

        val imageData = inputData.getByteArray("image_data")
        val memberId = inputData.getInt("member_id", 0)
        val loanId = inputData.getInt("loan_id", 0)
        val fileName = inputData.getString("file_name")!!

        ApiConnections.authenticate(
            preferences,
            context,
            ApiConnections.REQUEST_TYPE_POST_PICTURE,
            imageData,
            memberId = memberId,
            loanId = loanId,
            fileName = fileName
        )

        return Result.success()
    }
}