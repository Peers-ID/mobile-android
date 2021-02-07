package com.android.id.peers.util.callback

import com.android.id.peers.anggota.StatusPinjaman

interface StatusPinjamanCallback {
    fun onSuccess(result: List<StatusPinjaman>)
}