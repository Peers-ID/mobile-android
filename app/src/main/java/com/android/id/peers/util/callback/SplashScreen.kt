package com.android.id.peers.util.callback

import com.android.id.peers.members.models.MemberAcquisitionConfig

public interface SplashScreen/*: LoanFormulaCallback*/ {
    public fun onSuccess(result: MemberAcquisitionConfig)
//    public fun onSuccess2(result: LoanFormulaConfig)
//    public fun onSuccess3(result: List<OtherFees>)
}