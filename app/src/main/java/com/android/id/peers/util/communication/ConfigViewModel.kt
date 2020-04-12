package com.android.id.peers.util.communication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.id.peers.members.models.MemberAcquisitionConfig

class ConfigViewModel : ViewModel() {
    val config = MutableLiveData<MemberAcquisitionConfig>()

    fun setConfig(_config: MemberAcquisitionConfig) {
        config.value = _config
    }
}