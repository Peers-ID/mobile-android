package com.android.id.peers.util.callback

import com.android.id.peers.members.models.Member

interface MembersCallback {
    fun onSuccess(result: ArrayList<Member>)
}