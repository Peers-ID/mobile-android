package com.android.id.peers.util.callback

import com.android.id.peers.loans.models.LoanFormulaConfig
import com.android.id.peers.loans.models.OtherFees

interface LoanFormulaCallback {
    public fun onSuccess(result: LoanFormulaConfig)
    public fun onSuccess(result: List<OtherFees>)
}