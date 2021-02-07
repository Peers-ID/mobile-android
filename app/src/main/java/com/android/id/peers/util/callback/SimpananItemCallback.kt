package com.android.id.peers.util.callback

import com.android.id.peers.simpanan.Simpanan

interface SimpananItemCallback {
    fun onSuccess(result: ArrayList<Simpanan>)
}