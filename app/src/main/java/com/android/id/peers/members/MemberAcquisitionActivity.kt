package com.android.id.peers.members

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.id.peers.R
import kotlinx.android.synthetic.main.activity_member_acquisition.*

class MemberAcquisitionActivity : AppCompatActivity(), PersonalInformationFragment.OnFragmentInteractionListener,
    AddressFragment.OnFragmentInteractionListener, OccupationFragment.OnFragmentInteractionListener,
    EmergencyContactFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        super.onBackPressed()
        step_view.go(step_view.currentStep - 1, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_acquisition)
        title = "Member Acquisition"
    }
}
