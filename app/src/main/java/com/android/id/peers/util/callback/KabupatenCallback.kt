package com.android.id.peers.util.callback

import com.android.id.peers.members.models.Desa
import com.android.id.peers.members.models.Kabupaten
import com.android.id.peers.members.models.Kecamatan
import com.android.id.peers.members.models.Province

interface KabupatenCallback {
    fun onSuccess(result: List<Kabupaten>)
//    public fun onSuccess3(result: List<Desa>)
}