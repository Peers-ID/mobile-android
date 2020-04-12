package com.android.id.peers.members

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.id.peers.R
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.members.models.Member
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.fragment_address.*

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

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("alamat_ktp_jalan", 1) == 0) address_street.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_nomer", 1) == 0) address_no.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_rt", 1) == 0) address_rt.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_rw", 1) == 0) address_rw.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_kelurahan", 1) == 0) address_kelurahan.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_kecamatan", 1) == 0) address_kecamatan.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_status_tempat_tinggal", 1) == 0) address_status.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_lama_tinggal", 1) == 0) {
            how_long_text.visibility = View.GONE
            address_how_long_month.visibility = View.GONE
            address_how_long_year.visibility = View.GONE
        }
        if (configPreferences.getInt("domisili_sesuai_ktp", 1) == 0) address_domisili_equal_ktp.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_jalan", 1) == 0) address_street_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_nomer", 1) == 0) address_no_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_rt", 1) == 0) address_rt_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_rw", 1) == 0) address_rw_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_kelurahan", 1) == 0) address_kelurahan_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_kecamatan", 1) == 0) address_kecamatan_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_kota_provinsi", 1) == 0) {
            address_city_domisili.visibility = View.GONE
            address_province_domisili.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_domisili_status_tempat_tinggal", 1) == 0) address_status_domisili.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_lama_tempat_tinggal", 1) == 0) {
            how_long_domisili_text.visibility = View.GONE
            address_how_long_domisili_month.visibility = View.GONE
            address_how_long_domisili_year.visibility = View.GONE
        }

        val addressStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.address_status))
        addressStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        address_status.adapter = addressStatusAdapter

        val addressStatusDomisiliAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.address_status))
        addressStatusDomisiliAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        address_status_domisili.adapter = addressStatusDomisiliAdapter
        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            address_street.setText(member.jalanSesuaiKtp)
            address_no.setText(member.nomorSesuaiKtp)
            address_rt.setText(member.rtSesuaiKtp)
            address_rw.setText(member.rwSesuaiKtp)
            address_kelurahan.setText(member.kelurahanSesuaiKtp)
            address_kecamatan.setText(member.kecamatanSesuaiKtp)
            address_city.setText(member.kotaSesuaiKtp)
            address_province.setText(member.provinsiSesuaiKtp)

            address_status.selection = member.statusTempatTinggalSesuaiKtp
            address_how_long_month.setSelection(member.lamaBulanTinggalSesuaiKtp)
            address_how_long_year.setSelection(member.lamaTahunTinggalSesuaiKtp)
            address_domisili_equal_ktp.isChecked = member.domisiliSesuaiKtp
            address_street_domisili.setText(member.jalanDomisili)
            address_no_domisili.setText(member.nomorDomisili)
            address_rt_domisili.setText(member.rtDomisili)
            address_rw_domisili.setText(member.rwDomisili)
            address_kelurahan_domisili.setText(member.kelurahanDomisili)
            address_kecamatan_domisili.setText(member.kecamatanDomisili)
            address_city_domisili.setText(member.kotaDomisili)
            address_province.setText(member.provinsiDomisili)
            address_status_domisili.selection = member.statusTempatTinggalDomisili
            address_how_long_domisili_month.setSelection(member.lamaBulanTinggalDomisili)
            address_how_long_domisili_year.setSelection(member.lamaTahunTinggalDomisili)
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
//        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
//        stepView.go(0, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }
        setMember(member)

//        val fragment = PersonalInformationFragment()
//        val transaction = activity!!.supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
        activity!!.onBackPressed()
    }

    private fun setMember(member: Member) {
        member.jalanSesuaiKtp = address_street.text.toString()
        member.nomorSesuaiKtp = address_no.text.toString()
        member.rtSesuaiKtp = address_rt.text.toString()
        member.rwSesuaiKtp = address_rw.text.toString()
        member.kelurahanSesuaiKtp = address_kelurahan.text.toString()
        member.kecamatanSesuaiKtp = address_kecamatan.text.toString()
        member.kotaSesuaiKtp = address_city.text.toString()
        member.provinsiSesuaiKtp = address_province.text.toString()
        member.statusTempatTinggalSesuaiKtp = address_status.selection
        member.lamaBulanTinggalSesuaiKtp = address_how_long_month.selectedItemPosition
        member.lamaTahunTinggalSesuaiKtp = address_how_long_year.selectedItemPosition
        member.domisiliSesuaiKtp = address_domisili_equal_ktp.isChecked
        member.jalanDomisili = address_street_domisili.text.toString()
        member.nomorDomisili = address_no_domisili.text.toString()
        member.rtDomisili = address_rt_domisili.text.toString()
        member.rwDomisili = address_rw_domisili.text.toString()
        member.kelurahanDomisili = address_kelurahan_domisili.text.toString()
        member.kecamatanDomisili = address_kecamatan_domisili.text.toString()
        member.kotaDomisili = address_city_domisili.text.toString()
        member.provinsiDomisili = address_province_domisili.text.toString()
        member.statusTempatTinggalDomisili = address_status_domisili.selection
        member.lamaBulanTinggalDomisili = address_how_long_domisili_month.selectedItemPosition
        member.lamaTahunTinggalDomisili = address_how_long_domisili_year.selectedItemPosition
        memberViewModel.setMember(member)
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("alamat_ktp_jalan", 1) == 1 && address_street.text.toString().isEmpty()) {
            address_street_container.error = "Alamat sesuai KTP : Jalan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_nomer", 1) == 1 && address_no.text.toString().isEmpty()) {
            address_no_container.error = "Alamat sesuai KTP : No tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_rt", 1) == 1 && address_rt.text.toString().isEmpty()) {
            address_rt_container.error = "Alamat sesuai KTP : RT tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_rw", 1) == 1 && address_rw.text.toString().isEmpty()) {
            address_rw_container.error = "Alamat sesuai KTP : RW tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_kelurahan", 1) == 1 && address_kelurahan.text.toString().isEmpty()) {
            address_kelurahan_container.error = "Alamat sesuai KTP : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_kecamatan", 1) == 1 && address_kecamatan.text.toString().isEmpty()) {
            address_kecamatan_container.error = "Alamat sesuai KTP : Kecamatan tidak boleh kosong"
            allTrue = false
        }

        if(address_city.text.toString().isEmpty()) {
            address_city_container.error = "Alamat sesuai KTP : Kota tidak boleh kosong"
            allTrue = false
        }
        if(address_province.text.toString().isEmpty()) {
            address_province_container.error = "Alamat sesuai KTP : Provinsi tidak boleh kosong"
        }
        if(configPreferences.getInt("alamat_ktp_status_tempat_tinggal", 1) == 1 && address_status.selectedItemId < 0) {
            address_status.error = "Alamat sesuai KTP : Status Tempat Tinggal tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_lama_tinggal", 1) == 1) {
            if(address_how_long_month.selectedItemPosition == 0) {
                val errorText = address_how_long_month.selectedView as TextView
                errorText.setTextColor(Color.RED)
                errorText.error = "Bulan/Tahun harus dipilih"
                allTrue = false
            }
            if(address_how_long_year.selectedItemPosition == 0) {
                val errorText = address_how_long_year.selectedView as TextView
                errorText.setTextColor(Color.RED)
                errorText.error = "Bulan/Tahun harus dipilih"
                allTrue = false
            }
        }
        if(configPreferences.getInt("alamat_domisili_jalan", 1) == 1 && address_street_domisili.text.toString().isEmpty()) {
            address_street_domisili_container.error = "Alamat domisili : Jalan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_nomer", 1) == 1 && address_no_domisili.text.toString().isEmpty()) {
            address_no_domisili_container.error = "Alamat domisili : No tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_rt", 1) == 1 && address_rt_domisili.text.toString().isEmpty()) {
            address_rt_domisili_container.error = "Alamat domisili : RT tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_rw", 1) == 1 && address_rw_domisili.text.toString().isEmpty()) {
            address_rw_domisili_container.error = "Alamat domisili : RW tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_kelurahan", 1) == 1 && address_kelurahan_domisili.text.toString().isEmpty()) {
            address_kecamatan_domisili_container.error = "Alamat domisili : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_kecamatan", 1) == 1 && address_kecamatan_domisili.text.toString().isEmpty()) {
            address_kecamatan_domisili_container.error = "Alamat domisili : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_kota_provinsi", 1) == 1 && address_city_domisili.text.toString().isEmpty()) {
            address_city_domisili_container.error = "Alamat domisili : Kota tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_kota_provinsi", 1) == 1 && address_province_domisili.text.toString().isEmpty()) {
            address_province_domisili_container.error = "Alamat domisili : Provinsi tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_status_tempat_tinggal", 1) == 1 && address_status_domisili.selectedItemId < 0) {
            address_status_domisili.error = "Alamat domisili : Status Tempat Tinggal tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_lama_tempat_tinggal", 1) == 1) {
            if(address_how_long_domisili_month.selectedItemPosition == 0) {
                val errorText = address_how_long_domisili_month.selectedView as TextView
                errorText.setTextColor(Color.RED)
                errorText.error = "Bulan/Tahun harus dipilih"
                allTrue = false
            }
            if(address_how_long_domisili_year.selectedItemPosition == 0) {
                val errorText = address_how_long_domisili_year.selectedView as TextView
                errorText.setTextColor(Color.RED)
                errorText.error = "Bulan/Tahun harus dipilih"
                allTrue = false
            }
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

            setMember(member)

            val fragment = OccupationFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).addToBackStack(null).commit()
        }
    }

    private fun onCheckBoxChecked(view: View, isChecked: Boolean) {
        if(isChecked) {
            address_street_domisili.setText(address_street.text.toString())
            address_no_domisili.setText(address_no.text.toString())
            address_rt_domisili.setText(address_rt.text.toString())
            address_rw_domisili.setText(address_rw.text.toString())
            address_kelurahan_domisili.setText(address_kelurahan.text.toString())
            address_kecamatan_domisili.setText(address_kecamatan.text.toString())
            address_city_domisili.setText(address_city.text.toString())
            address_province_domisili.setText(address_province.text.toString())
            address_status_domisili.selection = address_status.selection
            address_how_long_domisili_month.setSelection(address_how_long_month.selectedItemPosition)
            address_how_long_domisili_year.setSelection(address_how_long_year.selectedItemPosition)
        }else{
            address_street_domisili.setText("")
            address_no_domisili.setText("")
            address_rt_domisili.setText("")
            address_rw_domisili.setText("")
            address_kelurahan_domisili.setText("")
            address_kecamatan_domisili.setText("")
            address_city_domisili.setText("")
            address_status_domisili.selection = 0
            address_how_long_domisili_month.setSelection(0)
            address_how_long_domisili_year.setSelection(0)
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
