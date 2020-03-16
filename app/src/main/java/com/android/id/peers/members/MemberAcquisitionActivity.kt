package com.android.id.peers.members

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.id.peers.R

class MemberAcquisitionActivity : AppCompatActivity(), PersonalInformationFragment.OnFragmentInteractionListener,
    AddressFragment.OnFragmentInteractionListener, OccupationFragment.OnFragmentInteractionListener,
    EmergencyContactFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_acquisition)
        title = "Member Acquisition"
    }
}
