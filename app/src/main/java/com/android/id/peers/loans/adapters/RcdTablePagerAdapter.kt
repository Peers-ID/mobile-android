package com.android.id.peers.loans.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.id.peers.loans.*
import com.android.id.peers.members.models.Member

class RcdTablePagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        RcPersonalFragment(),
        RcAddressFragment(),
        RcOccupationFragment(),
        RcEmergencyFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Personal"
            1 -> "Address"
            2 -> "Occupation"
            else -> "Emergency"
        }
    }
}