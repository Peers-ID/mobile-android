package com.android.id.peers.util.callback

import com.android.id.peers.members.models.MemberNikStatus

interface MemberCallback {
    fun onSuccess(result: MemberNikStatus)
}