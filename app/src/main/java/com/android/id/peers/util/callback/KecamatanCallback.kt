package com.android.id.peers.util.callback

import com.android.id.peers.members.models.Kecamatan

interface KecamatanCallback {
    fun onSuccess(result: List<Kecamatan>)
}