package com.android.id.peers.members.communication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.id.peers.members.model.Member

class MemberViewModel : ViewModel() {
    val member = MutableLiveData<Member>()

    fun getMember(): LiveData<Member> {
        return member
    }

    fun setMember(_member: Member) {
        member.value = _member
    }
}