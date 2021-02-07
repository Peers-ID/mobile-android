package com.android.id.peers.util.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.id.peers.loans_unused.models.Collection
import com.android.id.peers.util.connection.ApiConnections

class CollectionWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    val context = appContext

    override fun doWork(): Result {

        val preferences = context.getSharedPreferences("login_data", Context.MODE_PRIVATE)

        val collection = Collection()
        collection.koperasiId = inputData.getInt("koperasi_id", 0)
        collection.memberId = inputData.getInt("member_id", 0)
        collection.aoId = inputData.getInt("ao_id", 0)
        collection.loanId = inputData.getInt("loan_id", 0)
        collection.cicilanJumlah = inputData.getLong("loan_cicilan", 0)
        collection.cicilanKe = inputData.getInt("cicilan_ke", 0)

        ApiConnections.authenticate(
            preferences,
            context, ApiConnections.REQUEST_TYPE_POST_COLLECTION, collection
        )

        return Result.success()
    }
}