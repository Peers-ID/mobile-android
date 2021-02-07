package com.android.id.peers.util.callback

import com.android.id.peers.loans_unused.models.Loan

interface LoanDisbursement {
    public fun onSuccess(result: List<Loan>)
}