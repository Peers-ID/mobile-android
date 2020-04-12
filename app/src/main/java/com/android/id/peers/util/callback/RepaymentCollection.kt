package com.android.id.peers.util.callback

import com.android.id.peers.members.models.Member

interface RepaymentCollection {
    public fun onSuccess(result: Member)
}