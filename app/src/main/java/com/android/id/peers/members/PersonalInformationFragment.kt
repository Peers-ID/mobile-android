package com.android.id.peers.members

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.id.peers.R
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.members.models.Member
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.fragment_personal_information.*
import java.text.SimpleDateFormat
import java.util.*

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

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("jenis_identitas", 1) == 0) identity_type.visibility = View.GONE
        if (configPreferences.getInt("no_identitas", 1) == 0) {
            identity_no.visibility = View.GONE
            identity_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("nama_lengkap", 1) == 0) {
            full_name.visibility = View.GONE
            full_name_container.visibility = View.GONE
        }
        if (configPreferences.getInt("no_hp", 1) == 0) {
            handphone_no.visibility = View.GONE
            handphone_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("tanggal_lahir", 1) == 0) {
            birth_date.visibility = View.GONE
            birth_place_container.visibility = View.GONE
        }
        if (configPreferences.getInt("tempat_lahir", 1) == 0) {
            birth_place.visibility = View.GONE
            birth_place_container.visibility = View.GONE
        }
        if (configPreferences.getInt("jenis_kelamin", 1) == 0) sex.visibility = View.GONE
        if (configPreferences.getInt("nama_gadis_ibu", 1) == 0) {
            mother_name.visibility = View.GONE
            mother_name_container.visibility = View.GONE
        }
        if (configPreferences.getInt("status_perkawinan", 1) == 0) marital_status.visibility = View.GONE
        if (configPreferences.getInt("pendidikan_terakhir", 1) == 0) last_education.visibility = View.GONE

        birth_date.keyListener = null
        birth_date.setOnClickListener { showDialog(view) }

        /* MaterialSpinner Adapters */
        val identityAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.identity))
        identityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        identity_type.adapter = identityAdapter
        val sexAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sex))
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sex.adapter = sexAdapter
        val maritalStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.marital_status))
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        marital_status.adapter = maritalStatusAdapter
        val lastEducationAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.last_education))
        lastEducationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        last_education.adapter = lastEducationAdapter

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
            member ->
            identity_type.selection = member.jenisIdentitas
            identity_no.setText(member.noIdentitas)
            full_name.setText(member.namaLengkap)
            handphone_no.setText(member.noHp)
            birth_date.setText(member.tanggalLahir)
            birth_place.setText(member.tempatLahir)
            sex.selection = member.jenisKelamin
            mother_name.setText(member.namaGadisIbuKandung)
            marital_status.selection = member.statusPerkawinan
            last_education.selection = member.pendidikanTerakhir
        })
        next.setOnClickListener { onNextButtonClicked(view) }
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("jenis_identitas", 1) == 1 && identity_type.selectedItemId < 0) {
            identity_type.error = "Jenis Identitas tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_identitas", 1) == 1 && identity_no.text.toString().isEmpty()) {
            identity_no_container.error = "No Identitas tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("nama_lengkap", 1) == 1 && full_name.text.toString().isEmpty()) {
            full_name_container.error = "Nama Lengkap tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_hp", 1) == 1 && handphone_no.text.toString().isEmpty()) {
            handphone_no_container.error = "No Handphone tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("tanggal_lahir", 1) == 1 && birth_date.text.toString().isEmpty()) {
            birth_date_container.error = "Tanggal Lahir tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("tempat_lahir", 1) == 1 && birth_place.text.toString().isEmpty()) {
            birth_place_container.error = "Tempat Lahir tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("jenis_kelamin", 1) == 1 && sex.selectedItemId < 0) {
            sex.error = "Jenis Kelamin tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("nama_gadis_ibu", 1) == 1 && mother_name.text.toString().isEmpty()) {
            mother_name_container.error = "Nama Gadis Ibu Kandung tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("status_perkawinan", 1) == 1 && marital_status.selectedItemId < 0) {
            marital_status.error = "Status Pernikahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("pendidikan_terakhir", 1) == 1 && last_education.selectedItemId < 0) {
            last_education.error = "Pendidikan Terakhir tidak boleh kosong"
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
            setMember(member)

            val fragment = AddressFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.member_acquisition_fragment_container, fragment)?.addToBackStack(null)?.commit()
        }
    }

    private fun setMember(member: Member) {
        member.jenisIdentitas = identity_type.selection
        member.noIdentitas = identity_no.text.toString()
        member.namaLengkap = full_name.text.toString()
        member.noHp = handphone_no.text.toString()
        member.tanggalLahir = birth_date.text.toString()
        member.tempatLahir = birth_place.text.toString()
        member.jenisKelamin = sex.selection
        member.namaGadisIbuKandung = mother_name.text.toString()
        member.statusPerkawinan = marital_status.selection
        member.pendidikanTerakhir = last_education.selection

        memberViewModel.setMember(member)
    }

    fun showDialog(_view: View) {
        val c = Calendar.getInstance()
        val _year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(_view.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            val placeholder = sdf.format(c.time)
            birth_date.setText(placeholder)
        }, _year, month, day)

        datePickerDialog.show()
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
