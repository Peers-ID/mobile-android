package com.android.id.peers.members

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.id.peers.R
import com.android.id.peers.members.communication.MemberViewModel
import com.android.id.peers.members.models.Member
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EmergencyContactFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EmergencyContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmergencyContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        memberViewModel = ViewModelProvider(activity!!).get(MemberViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nextButton = view.findViewById<Button>(R.id.next)
        nextButton.setOnClickListener { onNextButtonClicked(view) }
        val backButton = view.findViewById<Button>(R.id.back)
        backButton.setOnClickListener { onBackButtonClicked(view) }

        val relationship = view.findViewById<MaterialSpinner>(R.id.relationship)
        var relationshipAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.relationship))
        relationshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        relationship.adapter = relationshipAdapter

        val emergencyName = view.findViewById<TextInputEditText>(R.id.emergency_name)
        val handphoneNo = view.findViewById<TextInputEditText>(R.id.emergency_handphone_no)
//        val relationship = view.findViewById<MaterialSpinner>(R.id.relationship)

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->

            emergencyName.setText(member.namaEmergency)
            handphoneNo.setText(member.noHpEmergency)
            relationship.selection = member.hubunganEmergeny

        })
    }

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(2, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        val emergencyName = view.findViewById<TextInputEditText>(R.id.emergency_name)
        val handphoneNo = view.findViewById<TextInputEditText>(R.id.emergency_handphone_no)
        val relationship = view.findViewById<MaterialSpinner>(R.id.relationship)

        member.namaEmergency = emergencyName.text.toString()
        member.noHpEmergency = handphoneNo.text.toString()
        member.hubunganEmergeny = relationship.selection

        memberViewModel.setMember(member)

        val fragment = OccupationFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
    }

    private fun onNextButtonClicked(view: View) {
        val emergencyName = view.findViewById<TextInputEditText>(R.id.emergency_name)
        val emergencyNameC = view.findViewById<TextInputLayout>(R.id.emergency_name_container)
        val handphoneNo = view.findViewById<TextInputEditText>(R.id.emergency_handphone_no)
        val handphoneNoC = view.findViewById<TextInputLayout>(R.id.handphone_no_container)
        val relationship = view.findViewById<MaterialSpinner>(R.id.relationship)

        var allTrue = true

        if(emergencyName.text.toString().isEmpty()) {
            emergencyNameC.error = "Bidang Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(handphoneNo.text.toString().isEmpty()) {
            handphoneNoC.error = "Bidang Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(relationship.selectedItemId < 0) {
            relationship.error = "Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(allTrue) {
//            val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//            memberStatusView.statusView.run {
//                currentCount += 1
//            }

            var member = memberViewModel.member.value
            if(member == null) {
                member = Member()
            }

            member.namaEmergency = emergencyName.text.toString()
            member.noHpEmergency = handphoneNo.text.toString()
            member.hubunganEmergeny = relationship.selection

            memberViewModel.setMember(member)


            val intent = Intent(activity, MemberAcquisitionConfirmationActivity::class.java)
            intent.putExtra("member", memberViewModel.member.value)
            startActivity(intent)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EmergencyContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmergencyContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
