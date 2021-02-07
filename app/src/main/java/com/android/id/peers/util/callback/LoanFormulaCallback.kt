package com.android.id.peers.util.callback

import com.android.id.peers.loans_unused.models.LoanFormulaConfig
import com.android.id.peers.loans_unused.models.OtherFees

interface LoanFormulaCallback {
    public fun onSuccess(result: LoanFormulaConfig)
    public fun onSuccess(result: List<OtherFees>)
}