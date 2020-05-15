package com.android.id.peers.util.callback

import com.android.id.peers.members.models.Province

interface ProvinceCallback {
    fun onSuccess(result: List<Province>)
}