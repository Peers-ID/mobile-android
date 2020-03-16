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
import android.widget.CheckBox
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.id.peers.R
import com.android.id.peers.members.communication.MemberViewModel
import com.android.id.peers.members.model.Member
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_personal_information.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddressFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddressFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addressStatus = view.findViewById<MaterialSpinner>(R.id.address_status)
        var addressStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.address_status))
        addressStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addressStatus.adapter = addressStatusAdapter

        val addressStatusDomisili = view.findViewById<MaterialSpinner>(R.id.address_status_domisili)
        var addressStatusDomisiliAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.address_status))
        addressStatusDomisiliAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addressStatusDomisili.adapter = addressStatusDomisiliAdapter

        val addressStreet = view.findViewById<TextInputEditText>(R.id.address_street)
        val addressNo = view.findViewById<TextInputEditText>(R.id.address_no)
        val addressRT = view.findViewById<TextInputEditText>(R.id.address_rt)
        val addressRW = view.findViewById<TextInputEditText>(R.id.address_rw)
        val addressKelurahan = view.findViewById<TextInputEditText>(R.id.address_kelurahan)
        val addressKecamatan = view.findViewById<TextInputEditText>(R.id.address_kecamatan)
        val addressCity = view.findViewById<TextInputEditText>(R.id.address_city)

//        val addressStatus = view.findViewById<MaterialSpinner>(R.id.address_status)
        val addressHowLongMonth = view.findViewById<Spinner>(R.id.address_how_long_month)
        val addressHowLongYear = view.findViewById<Spinner>(R.id.address_how_long_year)

        val addressStreetDomisili = view.findViewById<TextInputEditText>(R.id.address_street_domisili)
        val addressNoDomisili = view.findViewById<TextInputEditText>(R.id.address_no_domisili)
        val addressRTDomisili = view.findViewById<TextInputEditText>(R.id.address_rt_domisili)
        val addressRWDomisili = view.findViewById<TextInputEditText>(R.id.address_rw_domisili)
        val addressKelurahanDomisili = view.findViewById<TextInputEditText>(R.id.address_kelurahan_domisili)
        val addressKecamatanDomisili = view.findViewById<TextInputEditText>(R.id.address_kecamatan_domisili)
        val addressCityDomisili = view.findViewById<TextInputEditText>(R.id.address_city_domisili)

//        val addressStatusDomisili = view.findViewById<MaterialSpinner>(R.id.address_status_domisili)
        val addressHowLongMonthDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_month)
        val addressHowLongYearDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_year)

        val addressDomisiliEqualKtp = view.findViewById<CheckBox>(R.id.address_domisili_equal_ktp)

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->

            addressStreet.setText(member.jalanSesuaiKtp)
            addressNo.setText(member.nomorSesuaiKtp)
            addressRT.setText(member.rtSesuaiKtp)
            addressRW.setText(member.rwSesuaiKtp)
            addressKelurahan.setText(member.kelurahanSesuaiKtp)
            addressKecamatan.setText(member.kecamatanSesuaiKtp)
            addressCity.setText(member.kotaSesuaiKtp)
            addressStatus.selection = member.statusTempatTinggalSesuaiKtp
            addressHowLongMonth.setSelection(member.lamaBulanTinggalSesuaiKtp)
            addressHowLongYear.setSelection(member.lamaTahunTinggalSesuaiKtp)
            addressDomisiliEqualKtp.isChecked = member.domisiliSesuaiKtp
            addressStreetDomisili.setText(member.jalanDomisili)
            addressNoDomisili.setText(member.nomorDomisili)
            addressRTDomisili.setText(member.rtDomisili)
            addressRWDomisili.setText(member.rwDomisili)
            addressKelurahanDomisili.setText(member.kelurahanDomisili)
            addressKecamatanDomisili.setText(member.kecamatanDomisili)
            addressCityDomisili.setText(member.kotaDomisili)
            addressStatusDomisili.selection = member.statusTempatTinggalDomisili
            addressHowLongMonthDomisili.setSelection(member.lamaBulanTinggalDomisili)
            addressHowLongYearDomisili.setSelection(member.lamaTahunTinggalDomisili)

        })

        val nextButton = view.findViewById<Button>(R.id.next)
        nextButton.setOnClickListener { onNextButtonClicked(view) }
        val backButton = view.findViewById<Button>(R.id.back)
        backButton.setOnClickListener { onBackButtonClicked(view) }
        val checkBox = view.findViewById<CheckBox>(R.id.address_domisili_equal_ktp)
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->  onCheckBoxChecked(view, isChecked) }
    }

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(0, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        val addressStreet = view.findViewById<TextInputEditText>(R.id.address_street)
        val addressNo = view.findViewById<TextInputEditText>(R.id.address_no)
        val addressRT = view.findViewById<TextInputEditText>(R.id.address_rt)
        val addressRW = view.findViewById<TextInputEditText>(R.id.address_rw)
        val addressKelurahan = view.findViewById<TextInputEditText>(R.id.address_kelurahan)
        val addressKecamatan = view.findViewById<TextInputEditText>(R.id.address_kecamatan)
        val addressCity = view.findViewById<TextInputEditText>(R.id.address_city)

        val addressStatus = view.findViewById<MaterialSpinner>(R.id.address_status)
        val addressHowLongMonth = view.findViewById<Spinner>(R.id.address_how_long_month)
        val addressHowLongYear = view.findViewById<Spinner>(R.id.address_how_long_year)

        val addressStreetDomisili = view.findViewById<TextInputEditText>(R.id.address_street_domisili)
        val addressNoDomisili = view.findViewById<TextInputEditText>(R.id.address_no_domisili)
        val addressRTDomisili = view.findViewById<TextInputEditText>(R.id.address_rt_domisili)
        val addressRWDomisili = view.findViewById<TextInputEditText>(R.id.address_rw_domisili)
        val addressKelurahanDomisili = view.findViewById<TextInputEditText>(R.id.address_kelurahan_domisili)
        val addressKecamatanDomisili = view.findViewById<TextInputEditText>(R.id.address_kecamatan_domisili)
        val addressCityDomisili = view.findViewById<TextInputEditText>(R.id.address_city_domisili)

        val addressStatusDomisili = view.findViewById<MaterialSpinner>(R.id.address_status_domisili)
        val addressHowLongMonthDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_month)
        val addressHowLongYearDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_year)

        val addressDomisiliEqualKtp = view.findViewById<CheckBox>(R.id.address_domisili_equal_ktp)

        member.jalanSesuaiKtp = addressStreet.text.toString()
        member.nomorSesuaiKtp = addressNo.text.toString()
        member.rtSesuaiKtp = addressRT.text.toString()
        member.rwSesuaiKtp = addressRW.text.toString()
        member.kelurahanSesuaiKtp = addressKelurahan.text.toString()
        member.kecamatanSesuaiKtp = addressKecamatan.text.toString()
        member.kotaSesuaiKtp = addressCity.text.toString()
        member.statusTempatTinggalSesuaiKtp = addressStatus.selection
        member.lamaBulanTinggalSesuaiKtp = addressHowLongMonth.selectedItemPosition
        member.lamaTahunTinggalSesuaiKtp = addressHowLongYear.selectedItemPosition
        member.domisiliSesuaiKtp = addressDomisiliEqualKtp.isChecked
        member.jalanDomisili = addressStreetDomisili.text.toString()
        member.nomorDomisili = addressNoDomisili.text.toString()
        member.rtDomisili = addressRTDomisili.text.toString()
        member.rwDomisili = addressRWDomisili.text.toString()
        member.kelurahanDomisili = addressKelurahanDomisili.text.toString()
        member.kecamatanDomisili = addressKecamatanDomisili.text.toString()
        member.kotaDomisili = addressCityDomisili.text.toString()
        member.statusTempatTinggalDomisili = addressStatusDomisili.selection
        member.lamaBulanTinggalDomisili = addressHowLongMonthDomisili.selectedItemPosition
        member.lamaTahunTinggalDomisili = addressHowLongYearDomisili.selectedItemPosition

        val fragment = PersonalInformationFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
    }

    private fun onNextButtonClicked(view: View) {
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

        val addressStatus = view.findViewById<MaterialSpinner>(R.id.address_status)
        val addressHowLongMonth = view.findViewById<Spinner>(R.id.address_how_long_month)
        val addressHowLongYear = view.findViewById<Spinner>(R.id.address_how_long_year)

        val addressStreetDomisili = view.findViewById<TextInputEditText>(R.id.address_street_domisili)
        val addressStreetDomisiliC = view.findViewById<TextInputLayout>(R.id.address_street_domisili_container)
        val addressNoDomisili = view.findViewById<TextInputEditText>(R.id.address_no_domisili)
        val addressNoDomisiliC = view.findViewById<TextInputLayout>(R.id.address_no_domisili_container)
        val addressRTDomisili = view.findViewById<TextInputEditText>(R.id.address_rt_domisili)
        val addressRTDomisiliC = view.findViewById<TextInputLayout>(R.id.address_rt_domisili_container)
        val addressRWDomisili = view.findViewById<TextInputEditText>(R.id.address_rw_domisili)
        val addressRWDomisiliC = view.findViewById<TextInputLayout>(R.id.address_rw_domisili_container)
        val addressKelurahanDomisili = view.findViewById<TextInputEditText>(R.id.address_kelurahan_domisili)
        val addressKelurahanDomisiliC = view.findViewById<TextInputLayout>(R.id.address_kelurahan_domisili_container)
        val addressKecamatanDomisili = view.findViewById<TextInputEditText>(R.id.address_kecamatan_domisili)
        val addressKecamatanDomisiliC = view.findViewById<TextInputLayout>(R.id.address_kecamatan_domisili_container)
        val addressCityDomisili = view.findViewById<TextInputEditText>(R.id.address_city_domisili)
        val addressCityDomisiliC = view.findViewById<TextInputLayout>(R.id.address_city_domisili_container)

        val addressStatusDomisili = view.findViewById<MaterialSpinner>(R.id.address_status_domisili)
        val addressHowLongMonthDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_month)
        val addressHowLongYearDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_year)

        var allTrue = true

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
        if(addressStatus.selectedItemId < 0) {
            addressStatus.error = "Alamat sesuai KTP : Status Tempat Tinggal tidak boleh kosong"
            allTrue = false
        }
        if(addressStreetDomisili.text.toString().isEmpty()) {
            addressStreetDomisiliC.error = "Alamat domisili : Jalan tidak boleh kosong"
            allTrue = false
        }
        if(addressNoDomisili.text.toString().isEmpty()) {
            addressNoDomisiliC.error = "Alamat domisili : No tidak boleh kosong"
            allTrue = false
        }
        if(addressRTDomisili.text.toString().isEmpty()) {
            addressRTDomisiliC.error = "Alamat domisili : RT tidak boleh kosong"
            allTrue = false
        }
        if(addressRWDomisili.text.toString().isEmpty()) {
            addressRWDomisiliC.error = "Alamat domisili : RW tidak boleh kosong"
            allTrue = false
        }
        if(addressKelurahanDomisili.text.toString().isEmpty()) {
            addressKelurahanDomisiliC.error = "Alamat domisili : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(addressKecamatanDomisili.text.toString().isEmpty()) {
            addressKecamatanDomisiliC.error = "Alamat domisili : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(addressCityDomisili.text.toString().isEmpty()) {
            addressCityDomisiliC.error = "Alamat domisili : Kota/Provinsi tidak boleh kosong"
            allTrue = false
        }
        if(addressStatusDomisili.selectedItemId < 0) {
            addressStatusDomisili.error = "Alamat domisili : Status Tempat Tinggal tidak boleh kosong"
            allTrue = false
        }
        if(allTrue) {
//            val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//            memberStatusView.statusView.run {
//                currentCount += 1
//            }
            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(2, true)

            var member = memberViewModel.member.value
            if(member == null) {
                member = Member()
            }

            val addressDomisiliEqualKtp = view.findViewById<CheckBox>(R.id.address_domisili_equal_ktp)

            member.jalanSesuaiKtp = addressStreet.text.toString()
            member.nomorSesuaiKtp = addressNo.text.toString()
            member.rtSesuaiKtp = addressRT.text.toString()
            member.rwSesuaiKtp = addressRW.text.toString()
            member.kelurahanSesuaiKtp = addressKelurahan.text.toString()
            member.kecamatanSesuaiKtp = addressKecamatan.text.toString()
            member.kotaSesuaiKtp = addressCity.text.toString()
            member.statusTempatTinggalSesuaiKtp = addressStatus.selection
            member.lamaBulanTinggalSesuaiKtp = addressHowLongMonth.selectedItemPosition
            member.lamaTahunTinggalSesuaiKtp = addressHowLongYear.selectedItemPosition
            member.domisiliSesuaiKtp = addressDomisiliEqualKtp.isChecked
            member.jalanDomisili = addressStreetDomisili.text.toString()
            member.nomorDomisili = addressNoDomisili.text.toString()
            member.rtDomisili = addressRTDomisili.text.toString()
            member.rwDomisili = addressRWDomisili.text.toString()
            member.kelurahanDomisili = addressKelurahanDomisili.text.toString()
            member.kecamatanDomisili = addressKecamatanDomisili.text.toString()
            member.kotaDomisili = addressCityDomisili.text.toString()
            member.statusTempatTinggalDomisili = addressStatusDomisili.selection
            member.lamaBulanTinggalDomisili = addressHowLongMonthDomisili.selectedItemPosition
            member.lamaTahunTinggalDomisili = addressHowLongYearDomisili.selectedItemPosition

            memberViewModel.setMember(member)

            val fragment = OccupationFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
        }
    }

    private fun onCheckBoxChecked(view: View, isChecked: Boolean) {
        val addressStreet = view.findViewById<TextInputEditText>(R.id.address_street)
        val addressNo = view.findViewById<TextInputEditText>(R.id.address_no)
        val addressRT = view.findViewById<TextInputEditText>(R.id.address_rt)
        val addressRW = view.findViewById<TextInputEditText>(R.id.address_rw)
        val addressKelurahan = view.findViewById<TextInputEditText>(R.id.address_kelurahan)
        val addressKecamatan = view.findViewById<TextInputEditText>(R.id.address_kecamatan)
        val addressCity = view.findViewById<TextInputEditText>(R.id.address_city)

        val addressStatus = view.findViewById<MaterialSpinner>(R.id.address_status)
        val addressHowLongMonth = view.findViewById<Spinner>(R.id.address_how_long_month)
        val addressHowLongYear = view.findViewById<Spinner>(R.id.address_how_long_year)

        val addressStreetDomisili = view.findViewById<TextInputEditText>(R.id.address_street_domisili)
        val addressNoDomisili = view.findViewById<TextInputEditText>(R.id.address_no_domisili)
        val addressRTDomisili = view.findViewById<TextInputEditText>(R.id.address_rt_domisili)
        val addressRWDomisili = view.findViewById<TextInputEditText>(R.id.address_rw_domisili)
        val addressKelurahanDomisili = view.findViewById<TextInputEditText>(R.id.address_kelurahan_domisili)
        val addressKecamatanDomisili = view.findViewById<TextInputEditText>(R.id.address_kecamatan_domisili)
        val addressCityDomisili = view.findViewById<TextInputEditText>(R.id.address_city_domisili)

        val addressStatusDomisili = view.findViewById<MaterialSpinner>(R.id.address_status_domisili)
        val addressHowLongMonthDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_month)
        val addressHowLongYearDomisili = view.findViewById<Spinner>(R.id.address_how_long_domisili_year)

        if(isChecked) {
            addressStreetDomisili.setText(addressStreet.text.toString())
            addressNoDomisili.setText(addressNo.text.toString())
            addressRTDomisili.setText(addressRT.text.toString())
            addressRWDomisili.setText(addressRW.text.toString())
            addressKelurahanDomisili.setText(addressKelurahan.text.toString())
            addressKecamatanDomisili.setText(addressKecamatan.text.toString())
            addressCityDomisili.setText(addressCity.text.toString())
            addressStatusDomisili.selection = addressStatus.selection
            addressHowLongMonthDomisili.setSelection(addressHowLongMonth.selectedItemPosition)
            addressHowLongYearDomisili.setSelection(addressHowLongYear.selectedItemPosition)
        }else{
            addressStreetDomisili.setText("")
            addressNoDomisili.setText("")
            addressRTDomisili.setText("")
            addressRWDomisili.setText("")
            addressKelurahanDomisili.setText("")
            addressKecamatanDomisili.setText("")
            addressCityDomisili.setText("")
            addressStatusDomisili.selection = 0
            addressHowLongMonthDomisili.setSelection(0)
            addressHowLongYearDomisili.setSelection(0)
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
         * @return A new instance of fragment AddressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}