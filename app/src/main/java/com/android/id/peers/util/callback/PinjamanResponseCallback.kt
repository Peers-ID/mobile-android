package com.android.id.peers.util.callback

import com.android.id.peers.pinjaman.pengajuan.PinjamanResponse

interface PinjamanResponseCallback {
    fun onSuccess(result: PinjamanResponse)
}