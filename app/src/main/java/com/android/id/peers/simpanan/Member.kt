package com.android.id.peers.simpanan

import android.util.Log

data class Member(val name: String, val simpanan: List<Simpanan>) : ExpandableRecyclerViewAdapter.ExpandableGroup<Simpanan>() {
    override fun getExpandingItems(): List<Simpanan> {
        Log.d("Member", "LIST SIZE : ${simpanan.size}")
        return simpanan
    }

}