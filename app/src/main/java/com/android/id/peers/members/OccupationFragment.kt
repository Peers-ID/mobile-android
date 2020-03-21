package com.android.id.peers.members

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
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
 * [OccupationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OccupationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OccupationFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_occupation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nextButton = view.findViewById<Button>(R.id.next)
        nextButton.setOnClickListener { onNextButtonClicked(view) }
        val backButton = view.findViewById<Button>(R.id.back)
        backButton.setOnClickListener { onBackButtonClicked(view) }

        val npwpExist = view.findViewById<MaterialSpinner>(R.id.npwp_exist)
        var npwpExistAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.npwp_exist))
        npwpExistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        npwpExist.adapter = npwpExistAdapter

        val occupationStatus = view.findViewById<MaterialSpinner>(R.id.occupation_status)
        var occupationStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.occupation_status))
        occupationStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        occupationStatus.adapter = occupationStatusAdapter

//        val npwpExist = view.findViewById<MaterialSpinner>(R.id.npwp_exist)
        val npwpNo = view.findViewById<TextInputEditText>(R.id.npwp_no)
//        val occupationStatus = view.findViewById<MaterialSpinner>(R.id.occupation_status)
        val occupationField = view.findViewById<TextInputEditText>(R.id.occupation_field)
        val occupationPosition = view.findViewById<TextInputEditText>(R.id.occupation_position)
        val companyName = view.findViewById<TextInputEditText>(R.id.company_name)
        val occupationHowLongMonth = view.findViewById<Spinner>(R.id.work_how_long_month)
        val occupationHowLongYear = view.findViewById<Spinner>(R.id.work_how_long_year)
        val occupationRevenue = view.findViewById<TextInputEditText>(R.id.occupation_revenue)
        val addressStreet = view.findViewById<TextInputEditText>(R.id.address_street)
        val addressNo = view.findViewById<TextInputEditText>(R.id.address_no)
        val addressRT = view.findViewById<TextInputEditText>(R.id.address_rt)
        val addressRW = view.findViewById<TextInputEditText>(R.id.address_rw)
        val addressKelurahan = view.findViewById<TextInputEditText>(R.id.address_kelurahan)
        val addressKecamatan = view.findViewById<TextInputEditText>(R.id.address_kecamatan)
        val addressCity = view.findViewById<TextInputEditText>(R.id.address_city)

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->

            npwpExist.selection = member.memilikiNpwp
            npwpNo.setText(member.nomorNpwp)
            occupationStatus.selection = member.pekerjaan
            occupationField.setText(member.bidangPekerjaan)
            occupationPosition.setText(member.posisiPekerjaan)
            companyName.setText(member.namaPerusahaan)
            occupationHowLongMonth.setSelection(member.lamaBulanBekerja)
            occupationHowLongYear.setSelection(member.lamaTahunBekerja)
            occupationRevenue.setText(member.penghasilan)
            addressStreet.setText(member.jalanKantor)
            addressNo.setText(member.nomorKantor)
            addressRT.setText(member.rtKantor)
            addressRW.setText(member.rwKantor)
            addressKelurahan.setText(member.kelurahanKantor)
            addressKecamatan.setText(member.kecamatanKantor)
            addressCity.setText(member.kotaKantor)

        })

    }

    private fun onBackButtonClicked(view: View) {
        val npwpExist = view.findViewById<MaterialSpinner>(R.id.npwp_exist)
        val npwpNo = view.findViewById<TextInputEditText>(R.id.npwp_no)
        val occupationStatus = view.findViewById<MaterialSpinner>(R.id.occupation_status)
        val occupationField = view.findViewById<TextInputEditText>(R.id.occupation_field)
        val occupationPosition = view.findViewById<TextInputEditText>(R.id.occupation_position)
        val companyName = view.findViewById<TextInputEditText>(R.id.company_name)
        val occupationHowLongMonth = view.findViewById<Spinner>(R.id.work_how_long_month)
        val occupationHowLongYear = view.findViewById<Spinner>(R.id.work_how_long_year)
        val occupationRevenue = view.findViewById<TextInputEditText>(R.id.occupation_revenue)
        val addressStreet = view.findViewById<TextInputEditText>(R.id.address_street)
        val addressNo = view.findViewById<TextInputEditText>(R.id.address_no)
        val addressRT = view.findViewById<TextInputEditText>(R.id.address_rt)
        val addressRW = view.findViewById<TextInputEditText>(R.id.address_rw)
        val addressKelurahan = view.findViewById<TextInputEditText>(R.id.address_kelurahan)
        val addressKecamatan = view.findViewById<TextInputEditText>(R.id.address_kecamatan)
        val addressCity = view.findViewById<TextInputEditText>(R.id.address_city)

//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(1, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        member.memilikiNpwp = npwpExist.selection
        member.nomorNpwp = npwpNo.text.toString()
        member.pekerjaan = occupationStatus.selection
        member.bidangPekerjaan = occupationField.text.toString()
        member.posisiPekerjaan = occupationPosition.text.toString()
        member.namaPerusahaan = companyName.text.toString()
        member.lamaBulanBekerja = occupationHowLongMonth.selectedItemPosition
        member.lamaTahunBekerja = occupationHowLongYear.selectedItemPosition
        member.penghasilan = occupationRevenue.text.toString()
        member.jalanKantor = addressStreet.text.toString()
        member.nomorKantor = addressNo.text.toString()
        member.rtKantor = addressRT.text.toString()
        member.rwKantor = addressRW.text.toString()
        member.kelurahanKantor = addressKelurahan.text.toString()
        member.kecamatanKantor = addressKecamatan.text.toString()
        member.kotaKantor = addressCity.text.toString()

        memberViewModel.setMember(member)

        val fragment = AddressFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
    }

    private fun onNextButtonClicked(view: View) {
        val npwpExist = view.findViewById<MaterialSpinner>(R.id.npwp_exist)
        val npwpNo = view.findViewById<TextInputEditText>(R.id.npwp_no)
        val npwpNoC = view.findViewById<TextInputLayout>(R.id.npwp_no_container)
        val occupationStatus = view.findViewById<MaterialSpinner>(R.id.occupation_status)
        val occupationField = view.findViewById<TextInputEditText>(R.id.occupation_field)
        val occupationFieldC = view.findViewById<TextInputLayout>(R.id.occupation_field_container)
        val occupationPosition = view.findViewById<TextInputEditText>(R.id.occupation_position)
        val occupationPositionC = view.findViewById<TextInputLayout>(R.id.occupation_position_container)
        val companyName = view.findViewById<TextInputEditText>(R.id.company_name)
        val companyNameC = view.findViewById<TextInputLayout>(R.id.company_name_container)
        val occupationHowLongMonth = view.findViewById<Spinner>(R.id.work_how_long_month)
        val occupationHowLongYear = view.findViewById<Spinner>(R.id.work_how_long_year)
        val occupationRevenue = view.findViewById<TextInputEditText>(R.id.occupation_revenue)
        val occupationRevenueC = view.findViewById<TextInputLayout>(R.id.occupation_revenue_container)
        val addressStreet = view.findViewById<TextInputEditText>(R.id.address_street)
        val addressStreetC = view.findViewById<TextInputLayout>(R.id.address_street_container)
        val addressNo = view.findViewById<TextInputEditText>(R.id.address_no)
        val addressNoC = view.findViewById<TextInputLayout>(R.id.address_no_container)
        val addressRT = view.findViewById<TextInputEditText>(R.id.address_rt)
        val addressRTC = view.findViewById<TextInputLayout>(R.id.address_rt_container)
        val addressRW = view.findViewById<TextInputEditText>(R.id.address_rw)
        val addressRWC = view.findViewById<TextInputLayout>(R.id.address_rw_container)
        val addressKelurahan = view.findViewById<TextInputEditText>(R.id.address_kelurahan)
        val addressKelurahanC = view.findViewById<TextInputLayout>(R.id.address_kelurahan_container)
        val addressKecamatan = view.findViewById<TextInputEditText>(R.id.address_kecamatan)
        val addressKecamatanC = view.findViewById<TextInputLayout>(R.id.address_kecamatan_container)
        val addressCity = view.findViewById<TextInputEditText>(R.id.address_city)
        val addressCityC = view.findViewById<TextInputLayout>(R.id.address_city_container)

        var allTrue = true

        if(npwpExist.selectedItemId < 0) {
            npwpExist.error = "Memiliki NPWP harus dipilih"
            allTrue = false
        }
        if(npwpExist.selectedItemId < 1 && npwpNo.text.toString().isEmpty()) {
            npwpNoC.error = "Nomor NPWP tidak boleh kosong"
            allTrue = false
        }else{
            npwpNoC.error = ""
        }
        if(occupationStatus.selectedItemId < 0) {
            occupationStatus.error = "Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(occupationField.text.toString().isEmpty()) {
            occupationFieldC.error = "Bidang Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(occupationPosition.text.toString().isEmpty()) {
            occupationPositionC.error = "Posisi/Jabatan tidak boleh kosong"
            allTrue = false
        }
        if(companyName.text.toString().isEmpty()) {
            companyNameC.error = "Nama Perusahaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(occupationRevenue.text.toString().isEmpty()) {
            occupationRevenueC.error = "Penghasilan/Omset usaha tidak boleh kosong"
            allTrue = false
        }
        if(addressStreet.text.toString().isEmpty()) {
            addressStreetC.error = "Alamat sesuai KTP : Jalan tidak boleh kosong"
            allTrue = false
        }
        if(addressStreet.text.toString().isEmpty()) {
            addressStreetC.error = "Alamat sesuai KTP : Jalan tidak boleh kosong"
            allTrue = false
        }
        if(addressNo.text.toString().isEmpty()) {
            addressNoC.error = "Alamat sesuai KTP : No tidak boleh kosong"
            allTrue = false
        }
        if(addressRT.text.toString().isEmpty()) {
            addressRTC.error = "Alamat sesuai KTP : RT tidak boleh kosong"
            allTrue = false
        }
        if(addressRW.text.toString().isEmpty()) {
            addressRWC.error = "Alamat sesuai KTP : RW tidak boleh kosong"
            allTrue = false
        }
        if(addressKelurahan.text.toString().isEmpty()) {
            addressKelurahanC.error = "Alamat sesuai KTP : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(addressKecamatan.text.toString().isEmpty()) {
            addressKecamatanC.error = "Alamat sesuai KTP : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(addressCity.text.toString().isEmpty()) {
            addressCityC.error = "Alamat sesuai KTP : Kota/Provinsi tidak boleh kosong"
            allTrue = false
        }
        if(allTrue) {
//            val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//            memberStatusView.statusView.run {
//                currentCount += 1
//            }
            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(3, true)

            var member = memberViewModel.member.value
            if(member == null) {
                member = Member()
            }

            member.memilikiNpwp = npwpExist.selection
            member.nomorNpwp = npwpNo.text.toString()
            member.pekerjaan = occupationStatus.selection
            member.bidangPekerjaan = occupationField.text.toString()
            member.posisiPekerjaan = occupationPosition.text.toString()
            member.namaPerusahaan = companyName.text.toString()
            member.lamaBulanBekerja = occupationHowLongMonth.selectedItemPosition
            member.lamaTahunBekerja = occupationHowLongYear.selectedItemPosition
            member.penghasilan = occupationRevenue.text.toString()
            member.jalanKantor = addressStreet.text.toString()
            member.nomorKantor = addressNo.text.toString()
            member.rtKantor = addressRT.text.toString()
            member.rwKantor = addressRW.text.toString()
            member.kelurahanKantor = addressKelurahan.text.toString()
            member.kecamatanKantor = addressKecamatan.text.toString()
            member.kotaKantor = addressCity.text.toString()

            memberViewModel.setMember(member)

            val fragment = EmergencyContactFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
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
         * @return A new instance of fragment OccupationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OccupationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
