package com.android.id.peers.members

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.id.peers.R
import com.android.id.peers.members.communication.MemberViewModel
import com.android.id.peers.members.model.Member
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PersonalInformationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PersonalInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalInformationFragment : Fragment() {
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
        val view: View = inflater!!.inflate(R.layout.fragment_personal_information, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identityType = view.findViewById<MaterialSpinner>(R.id.identity_type)
        val identityNo = view.findViewById<TextView>(R.id.identity_no)
        val fullName = view.findViewById<TextView>(R.id.full_name)
        val birthDate = view.findViewById<TextView>(R.id.birth_date)
        val birthPlace = view.findViewById<TextView>(R.id.birth_place)
        val sex = view.findViewById<MaterialSpinner>(R.id.sex)
        val motherName = view.findViewById<TextView>(R.id.mother_name)
        val maritalStatus = view.findViewById<MaterialSpinner>(R.id.marital_status)
        val lastEducation = view.findViewById<MaterialSpinner>(R.id.last_education)

        /* MaterialSpinner Adapters */
        val identityAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.identity))
        identityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        identityType.adapter = identityAdapter
        val sexAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sex))
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sex.adapter = sexAdapter
        val maritalStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.marital_status))
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maritalStatus.adapter = maritalStatusAdapter
        val lastEducationAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.last_education))
        lastEducationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lastEducation.adapter = lastEducationAdapter

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
            member ->
            identityType.selection = member.jenisIdentitas
            identityNo.text = member.noIdentitas
            fullName.text = member.namaLengkap
            birthDate.text = member.tanggalLahir
            birthPlace.text = member.tempatLahir
            sex.selection = member.jenisKelamin
            motherName.text = member.namaGadisIbuKandung
            maritalStatus.selection = member.statusPernikahan
            lastEducation.selection = member.pendidikanTerakhir
        })

        val nextButton = view.findViewById<Button>(R.id.next)
        nextButton.setOnClickListener { onNextButtonClicked(view) }
    }

    private fun onNextButtonClicked(view: View) {
        val identityType = view.findViewById<MaterialSpinner>(R.id.identity_type)
        val motherNameC = view.findViewById<TextInputLayout>(R.id.mother_name_container)
        val motherName = view.findViewById<TextInputEditText>(R.id.mother_name)
        val identityNoC = view.findViewById<TextInputLayout>(R.id.identity_no_container)
        val identityNo = view.findViewById<TextInputEditText>(R.id.identity_no)
        val fullNameC = view.findViewById<TextInputLayout>(R.id.full_name_container)
        val fullName = view.findViewById<TextInputEditText>(R.id.full_name)
        val birthDateC = view.findViewById<TextInputLayout>(R.id.birth_date_container)
        val birthDate = view.findViewById<TextInputEditText>(R.id.birth_date)
        val birthPlaceC = view.findViewById<TextInputLayout>(R.id.birth_place_container)
        val birthPlace = view.findViewById<TextInputEditText>(R.id.birth_place)
        val sex = view.findViewById<MaterialSpinner>(R.id.sex)
        val maritalStatus = view.findViewById<MaterialSpinner>(R.id.marital_status)
        val lastEducation = view.findViewById<MaterialSpinner>(R.id.last_education)

        var allTrue = true

        if(identityType.selectedItemId < 0) {
            identityType.error = "Jenis Identitas tidak boleh kosong"
            allTrue = false
        }
        if(identityNo.text.toString().isEmpty()) {
            identityNoC.error = "No Identitas tidak boleh kosong"
            allTrue = false
        }
        if(fullName.text.toString().isEmpty()) {
            fullNameC.error = "Nama Lengkap tidak boleh kosong"
            allTrue = false
        }
        if(birthDate.text.toString().isEmpty()) {
            birthDateC.error = "Tanggal Lahir tidak boleh kosong"
            allTrue = false
        }
        if(birthPlace.text.toString().isEmpty()) {
            birthPlaceC.error = "Tempat Lahir tidak boleh kosong"
            allTrue = false
        }
        if(sex.selectedItemId < 0) {
            sex.error = "Jenis Kelamin tidak boleh kosong"
            allTrue = false
        }
        if(motherName.text.toString().isEmpty()) {
            motherNameC.error = "Nama Gadis Ibu Kandung tidak boleh kosong"
            allTrue = false
        }
        if(maritalStatus.selectedItemId < 0) {
            maritalStatus.error = "Status Pernikahan tidak boleh kosong"
            allTrue = false
        }
        if(lastEducation.selectedItemId < 0) {
            lastEducation.error = "Pendidikan Terakhir tidak boleh kosong"
            allTrue = false
        }
        if(allTrue) {
//            val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//            memberStatusView.statusView.run {
//                currentCount += 1
//            }
            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(1, true)

            var member = memberViewModel.member.value
            if(member == null) {
                member = Member()
            }
            member.jenisIdentitas = identityType.selection
            member.noIdentitas = identityNo.text.toString()
            member.namaLengkap = fullName.text.toString()
            member.tanggalLahir = birthDate.text.toString()
            member.tempatLahir = birthPlace.text.toString()
            member.jenisKelamin = sex.selection
            member.namaGadisIbuKandung = motherName.text.toString()
            member.statusPernikahan = maritalStatus.selection
            member.pendidikanTerakhir = lastEducation.selection

            memberViewModel.setMember(member)

            val fragment = AddressFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.member_acquisition_fragment_container, fragment)?.commit()
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
         * @return A new instance of fragment PersonalInformationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
