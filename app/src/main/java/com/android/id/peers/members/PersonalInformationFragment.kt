package com.android.id.peers.members

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.android.id.peers.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import fr.ganfra.materialspinner.MaterialSpinner
import params.com.stepview.StatusViewScroller

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        if(identityType.selectedItemPosition == 0) {
            identityType.error = "Jenis Identitas tidak boleh kosong"
        }
        if(identityNo.text.toString().isEmpty()) {
            identityNoC.error = "No Identitas tidak boleh kosong"
        }
        if(fullName.text.toString().isEmpty()) {
            fullNameC.error = "Nama Lengkap tidak boleh kosong"
        }
        if(birthDate.text.toString().isEmpty()) {
            birthDateC.error = "Tanggal Lahir tidak boleh kosong"
        }
        if(birthPlace.text.toString().isEmpty()) {
            birthPlaceC.error = "Tempat Lahir tidak boleh kosong"
        }
        if(sex.selectedItemPosition == 0) {
            sex.error = "Jenis Kelamin tidak boleh kosong"
        }
        if(motherName.text.toString().isEmpty()) {
            motherNameC.error = "Nama Gadis Ibu Kandung tidak boleh kosong"
        }
        if(maritalStatus.selectedItemPosition == 0) {
            maritalStatus.error = "Status Pernikahan tidak boleh kosong"
        }
        if(lastEducation.selectedItemPosition == 0) {
            lastEducation.error = "Pendidikan Terakhir tidak boleh kosong"
        }
        if(identityType.selectedItemPosition > 0 &&
            !identityNo.text.toString().isEmpty() &&
            !fullName.text.toString().isEmpty() &&
            !birthDate.text.toString().isEmpty() &&
            !birthPlace.text.toString().isEmpty() &&
            sex.selectedItemPosition > 0 &&
            !motherName.text.toString().isEmpty() &&
            maritalStatus.selectedItemPosition > 0 &&
            lastEducation.selectedItemPosition > 0) {
            val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
            memberStatusView.statusView.run {
                currentCount += 1
            }

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
