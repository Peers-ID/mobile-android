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
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.android.id.peers.R
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.members.models.Member
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.fragment_occupation.*
import kotlinx.android.synthetic.main.fragment_occupation.company_name
import kotlinx.android.synthetic.main.fragment_occupation.npwp_exist
import kotlinx.android.synthetic.main.fragment_occupation.npwp_no
import kotlinx.android.synthetic.main.fragment_occupation.occupation_field
import kotlinx.android.synthetic.main.fragment_occupation.occupation_position
import kotlinx.android.synthetic.main.fragment_occupation.occupation_revenue
import kotlinx.android.synthetic.main.fragment_occupation.occupation_status
import kotlinx.android.synthetic.main.layout_occupation.*
import kotlinx.android.synthetic.main.layout_office_address.*

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

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("memiliki_npwp", 1) == 0) npwp_exist.visibility = View.GONE
        if (configPreferences.getInt("nomer_npwp", 1) == 0) npwp_no.visibility = View.GONE
        if (configPreferences.getInt("pekerja_usaha", 1) == 0) occupation.visibility = View.GONE
        if (configPreferences.getInt("bidang_pekerja", 1) == 0) occupation_field.visibility = View.GONE
        if (configPreferences.getInt("posisi_jabatan", 1) == 0) occupation_position.visibility = View.GONE
        if (configPreferences.getInt("nama_perusahaan", 1) == 0) company_name.visibility = View.GONE
        if (configPreferences.getInt("lama_bekerja", 1) == 0) {
            work_how_long_text.visibility = View.GONE
            work_how_long_month.visibility = View.GONE
            work_how_long_year.visibility = View.GONE
        }
        if (configPreferences.getInt("penghasilan_omset", 1) == 0) occupation_revenue.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_jalan", 1) == 0) office_address_street.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_nomer", 1) == 0) office_address_no.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_rt", 1) == 0) office_address_rt.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_rw", 1) == 0) office_address_rw.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_kelurahan", 1) == 0) office_address_kelurahan.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_kecamatan", 1) == 0) office_address_kecamatan.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_kota_provinsi", 1) == 0) {
            office_address_city.visibility = View.GONE
            office_address_province.visibility = View.GONE
        }

        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        val npwpExistAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.npwp_exist))
        npwpExistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        npwp_exist.adapter = npwpExistAdapter

        val occupationStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.occupation_status))
        occupationStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        occupation_status.adapter = occupationStatusAdapter

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            npwp_exist.selection = member.memilikiNpwp
            npwp_no.setText(member.nomorNpwp)
            occupation_status.selection = member.pekerjaan
            occupation_field.setText(member.bidangPekerjaan)
            occupation_position.setText(member.posisiPekerjaan)
            company_name.setText(member.namaPerusahaan)
            work_how_long_month.setSelection(member.lamaBulanBekerja)
            work_how_long_year.setSelection(member.lamaTahunBekerja)
            occupation_revenue.setText(member.penghasilan.toString())
            address_street.setText(member.jalanKantor)
            address_no.setText(member.nomorKantor)
            address_rt.setText(member.rtKantor)
            address_rw.setText(member.rwKantor)
            address_kelurahan.setText(member.kelurahanKantor)
            address_kecamatan.setText(member.kecamatanKantor)
            address_city.setText(member.kotaKantor)
        })

    }

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
//        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
//        stepView.go(1, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        setMember(member)

//        val fragment = AddressFragment()
//        val transaction = activity!!.supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
        activity?.onBackPressed()
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("memiliki_npwp", 1) == 1 && npwp_exist.selectedItemId < 0) {
            npwp_exist.error = "Memiliki NPWP harus dipilih"
            allTrue = false
        }
        if(configPreferences.getInt("nomer_npwp", 1) == 1 && npwp_exist.selectedItemId < 1 && npwp_no.text.toString().isEmpty()) {
            npwp_no_container.error = "Nomor NPWP tidak boleh kosong"
            allTrue = false
        }else{
            npwp_no_container.error = ""
        }
        if(configPreferences.getInt("pekerja_usaha", 1) == 1 && occupation_status.selectedItemId < 0) {
            occupation_status.error = "Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("bidang_pekerja", 1) == 1 && occupation_field.text.toString().isEmpty()) {
            occupation_field_container.error = "Bidang Pekerjaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("posisi_jabatan", 1) == 1 && occupation_position.text.toString().isEmpty()) {
            occupation_position_container.error = "Posisi/Jabatan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("nama_perusahaan", 1) == 1 && company_name.text.toString().isEmpty()) {
            company_name_container.error = "Nama Perusahaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("bidang_pekerja", 1) == 1) {
            if(work_how_long_month.selectedItemPosition == 0) {
                val errorText = work_how_long_month.selectedView as TextView
                errorText.setTextColor(Color.RED)
                errorText.error = "Bulan/Tahun harus dipilih"
                allTrue = false
            }
            if(work_how_long_year.selectedItemPosition == 0) {
                val errorText = work_how_long_year.selectedView as TextView
                errorText.setTextColor(Color.RED)
                errorText.error = "Bulan/Tahun harus dipilih"
                allTrue = false
            }
        }
        if(configPreferences.getInt("penghasilan_omset", 1) == 1 && occupation_revenue.text.toString().isEmpty()) {
            occupation_revenue_container.error = "Penghasilan/Omset usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_jalan", 1) == 1 && address_street.text.toString().isEmpty()) {
            address_street_container.error = "Alamat Kantor : Jalan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_nomer", 1) == 1 && address_no.text.toString().isEmpty()) {
            address_no_container.error = "Alamat Kantor : No tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_rt", 1) == 1 && address_rt.text.toString().isEmpty()) {
            address_rt_container.error = "Alamat Kantor : RT tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_rw", 1) == 1 && address_rw.text.toString().isEmpty()) {
            address_rw_container.error = "Alamat Kantor : RW tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_kelurahan", 1) == 1 && address_kelurahan.text.toString().isEmpty()) {
            address_kelurahan_container.error = "Alamat Kantor : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_kecamatan", 1) == 1 && address_kecamatan.text.toString().isEmpty()) {
            address_kecamatan_container.error = "Alamat Kantor : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_kota_provinsi", 1) == 1) {
            if(address_city.text.toString().isEmpty()) {
                address_city_container.error = "Alamat Kantor : Kota tidak boleh kosong"
                allTrue = false
            }
            if(address_province.text.toString().isEmpty()) {
                address_province_container.error = "Alamat Kantor : Provinsi tidak boleh kosong"
                allTrue = false
            }
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

            setMember(member)
            val fragment = EmergencyContactFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).addToBackStack(null).commit()
        }
    }

    private fun setMember(member: Member) {
        member.memilikiNpwp = npwp_exist.selection
        member.nomorNpwp = npwp_no.text.toString()
        member.pekerjaan = occupation_status.selection
        member.bidangPekerjaan = occupation_field.text.toString()
        member.posisiPekerjaan = occupation_position.text.toString()
        member.namaPerusahaan = company_name.text.toString()
        member.lamaBulanBekerja = work_how_long_month.selectedItemPosition
        member.lamaTahunBekerja = work_how_long_year.selectedItemPosition
        member.penghasilan = occupation_revenue.text.toString().toLong()
        member.jalanKantor = address_street.text.toString()
        member.nomorKantor = address_no.text.toString()
        member.rtKantor = address_rt.text.toString()
        member.rwKantor = address_rw.text.toString()
        member.kelurahanKantor = address_kelurahan.text.toString()
        member.kecamatanKantor = address_kecamatan.text.toString()
        member.kotaKantor = address_city.text.toString()
        member.provinsiKantor = address_province.text.toString()

        memberViewModel.setMember(member)
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
