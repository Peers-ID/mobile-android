package com.android.id.peers.util.callback

import com.android.id.peers.loans.models.LoanFormulaConfig
import com.android.id.peers.loans.models.OtherFees
import com.android.id.peers.members.models.MemberAcquisitionConfig

public interface SplashScreen: LoanApplication {
    public fun onSuccess(result: MemberAcquisitionConfig)
    public override fun onSuccess(result: LoanFormulaConfig)
    public override fun onSuccess(result: List<OtherFees>)
}