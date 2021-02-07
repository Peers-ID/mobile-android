package com.android.id.peers.pinjaman.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.communication.MemberViewModel
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.button_bottom.*
import kotlinx.android.synthetic.main.fragment_emergency_contact.*

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
//    private var listener: OnFragmentInteractionListener? = null

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

        Log.d("EmergencyFragment", " NO HP : ${memberViewModel.member.value!!.noHp}")

        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("nama_emergency", 1) == 0) {
            emergency_name.visibility = View.GONE
            emergency_name_container.visibility = View.GONE
        }
        if (configPreferences.getInt("no_hp_emergency", 1) == 0) {
            handphone_no.visibility = View.GONE
            handphone_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("hubungan", 1) == 0) relationship.visibility = View.GONE

        val relationshipAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.relationship))
        relationshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        relationship.adapter = relationshipAdapter

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            emergency_name.setText(member.namaEmergency)
            handphone_no.setText(member.noHpEmergency)
            relationship.selection = member.hubunganEmergency
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

        member.namaEmergency = emergency_name.text.toString()
        member.noHpEmergency = handphone_no.text.toString()
        member.hubunganEmergency = relationship.selection

        memberViewModel.setMember(member)

        val fragment = OccupationFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
//        activity!!.onBackPressed()
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("nama_emergency", 1) == 1 &&emergency_name.text.toString().isEmpty()) {
            emergency_name_container.error = "Nama tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_hp_emergency", 1) == 1 && handphone_no.text.toString().isEmpty()) {
            handphone_no_container.error = "Nomor HP tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("hubungan", 1) == 1 && relationship.selectedItemId < 0) {
            relationship.error = "Hubungan tidak boleh kosong"
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

            member.namaEmergency = emergency_name.text.toString()
            member.noHpEmergency = handphone_no.text.toString()
            member.hubunganEmergency = relationship.selection

            memberViewModel.setMember(member)

            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(4, true)

            val fragment = DataPasanganFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()

//            val intent = Intent(activity, MemberAcquisitionConfirmationActivity::class.java)
//            intent.putExtra("member", memberViewModel.member.value)
//            startActivity(intent)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

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
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

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
