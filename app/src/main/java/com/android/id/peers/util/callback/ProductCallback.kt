package com.android.id.peers.util.callback

import com.android.id.peers.pinjaman.pengajuan.Product

interface ProductCallback {
    fun onSuccess(result: List<Product>)
}
