package com.android.id.peers.util.callback

import com.android.id.peers.pinjaman.pencairan.PencairanPinjaman

interface PencairanCallback {
    fun onSuccess(result : PencairanPinjaman)
}