package com.android.id.peers.pinjaman.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.*
import com.android.id.peers.util.CurrencyFormat
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.util.database.OfflineViewModel
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.button_bottom.*
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_occupation.*
import kotlinx.android.synthetic.main.fragment_occupation.address_city
import kotlinx.android.synthetic.main.fragment_occupation.address_kecamatan
import kotlinx.android.synthetic.main.fragment_occupation.address_kelurahan
import kotlinx.android.synthetic.main.fragment_occupation.address_kode_pos
import kotlinx.android.synthetic.main.fragment_occupation.address_kode_pos_container
import kotlinx.android.synthetic.main.fragment_occupation.address_province
import kotlinx.android.synthetic.main.fragment_occupation.address_street
import kotlinx.android.synthetic.main.fragment_occupation.address_street_container
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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
class OccupationFragment : Fragment(), CoroutineScope {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
//    private var listener: OnFragmentInteractionListener? = null
    private lateinit var offlineViewModel: OfflineViewModel
    private var provinces: List<Province> = ArrayList()
    private var kabupatens: List<Kabupaten> = ArrayList()
    private var kecamatans: List<Kecamatan> = ArrayList()
    private var kelurahans: List<Desa> = ArrayList()
    private lateinit var provinceAdapter: ArrayAdapter<Province>
    private lateinit var cityAdapter: ArrayAdapter<Kabupaten>
    private lateinit var kecamatanAdapter: ArrayAdapter<Kecamatan>
    private lateinit var kelurahanAdapter: ArrayAdapter<Desa>
    private val coroutineScope = this

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob()

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext[Job]!!.cancel()
    }

    override fun onPause() {
        super.onPause()
        Log.d("PersonalInfo", "PAUSE FRAGMENT")
        coroutineContext[Job]!!.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        memberViewModel = ViewModelProvider(activity!!).get(MemberViewModel::class.java)
        offlineViewModel = ViewModelProvider(activity!!).get(OfflineViewModel::class.java)
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

        Log.d("OccupationFragment", " NO HP : ${memberViewModel.member.value!!.noHp}")


        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        Log.d("OccupationFragment",configPreferences.all.toString())

        if(configPreferences.getInt("memiliki_npwp",1) == 0) {
            npwp_no_container.visibility = View.VISIBLE
        }else {
            npwp_no_container.visibility = View.GONE
        }

        if(configPreferences.getInt("pekerja_usaha",1) == 6) {
            jenis_umkm_container.visibility = View.VISIBLE
        }else {
            jenis_umkm_container.visibility = View.GONE
        }

        if (configPreferences.getInt("memiliki_npwp", 1) == 0) npwp_exist.visibility = View.GONE

        if (configPreferences.getInt("nomer_npwp", 1) == 0) {
            npwp_no.visibility = View.GONE
            npwp_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("pekerja_usaha", 1) == 0) occupation_status.visibility = View.GONE
//        if (configPreferences.getInt("bidang_pekerja", 1) == 0) {
//            occupation_field.visibility = View.GONE
//            occupation_field_container.visibility = View.GONE
//        }
        if (configPreferences.getInt("jenis_umkm", 1) == 0) {
            jenis_umkm.visibility = View.GONE
            jenis_umkm_container.visibility = View.GONE
        }
        if (configPreferences.getInt("nama_perusahaan", 1) == 0) {
            company_name.visibility = View.GONE
            company_name_container.visibility = View.GONE
        }
        if (configPreferences.getInt("lama_bekerja", 1) == 0) {
            work_how_long_text.visibility = View.GONE
            work_how_long_month.visibility = View.GONE
            work_how_long_year.visibility = View.GONE
        }
        if (configPreferences.getInt("penghasilan_omset", 1) == 0) {
            occupation_revenue.visibility = View.GONE
            occupation_revenue_container.visibility = View.GONE
        }

        var textBefore = ""
        occupation_revenue.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                // 1. get cursor position : p0 = start + before
                val initialCursorPosition = start + before
                //2. get digit count after cursor position : c0
                val numOfDigitsToRightOfCursor = CurrencyFormat.getNumberOfDigits(textBefore.substring(initialCursorPosition,
                    textBefore.length))

                val newAmount = CurrencyFormat.formatAmount(s.toString())
                occupation_revenue.removeTextChangedListener(this)
                occupation_revenue.setText(newAmount)
                //set new cursor position
                occupation_revenue.setSelection(CurrencyFormat.getNewCursorPosition(numOfDigitsToRightOfCursor, newAmount))
                occupation_revenue.addTextChangedListener(this)
            }

        })

        if (configPreferences.getInt("alamat_kantor_jalan", 1) == 0) {
            address_street.visibility = View.GONE
            address_street_container.visibility = View.GONE
        }
//        if (configPreferences.getInt("alamat_kantor_nomer", 1) == 0) {
//            address_no.visibility = View.GONE
//            address_no_container.visibility = View.GONE
//        }
//        if (configPreferences.getInt("alamat_kantor_rt", 1) == 0) {
//            address_rt.visibility = View.GONE
//            address_rt_container.visibility = View.GONE
//        }
//        if (configPreferences.getInt("alamat_kantor_rw", 1) == 0) {
//            address_rw.visibility = View.GONE
//            address_rw_container.visibility = View.GONE
//        }
//        if (configPreferences.getInt("alamat_kantor_kelurahan", 1) == 0) {
//            address_kelurahan.visibility = View.GONE
//            address_kelurahan_container.visibility = View.GONE
//        }
        if (configPreferences.getInt("alamat_kantor_kelurahan", 1) == 0) address_kelurahan.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_kecamatan", 1) == 0) address_kecamatan.visibility = View.GONE
        if (configPreferences.getInt("alamat_kantor_kota", 1) == 0) {
            address_city.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_kantor_provinsi", 1) == 0) {
            address_province.visibility = View.GONE
        }

        if (configPreferences.getInt("alamat_kantor_kode_pos", 1) == 0) {
            address_kode_pos.visibility = View.GONE
            address_kode_pos_container.visibility = View.GONE
        }

        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        val npwpExistAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.npwp_exist))
        npwpExistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        npwp_exist.adapter = npwpExistAdapter

        val occupationStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.occupation_status))
        occupationStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        occupation_status.adapter = occupationStatusAdapter

        provinceAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, provinces)
        cityAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kabupatens)
        kecamatanAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kecamatans)
        kelurahanAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kelurahans)
//            val arrayAdapter = RegionAdapter(context!!, provinces, "Province")
        address_province.adapter = provinceAdapter
        address_city.adapter = cityAdapter
        address_kecamatan.adapter = kecamatanAdapter
        address_kelurahan.adapter = kelurahanAdapter

        offlineViewModel.allProvince.observe(viewLifecycleOwner, Observer {provinces ->
            this.provinces = provinces
            Log.d("OccupationFragment", "SIZE PROVINCES : ${provinces.size}")
            Log.d("OccupationFragment", "SIZE PROVINCES ADAPTER : ${this.provinces.size}")
            provinceAdapter.clear()
            provinceAdapter.addAll(provinces)
            provinceAdapter.notifyDataSetChanged()
        })

        npwp_exist.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                npwp_no_container.visibility = if(position == 0) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
//                TODO("Not yet implemented")
            }
        }

        occupation_status.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                jenis_umkm_container.visibility = if(position == 6) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
//                TODO("Not yet implemented")
            }

        }

        address_province.onItemSelectedListener = object: MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                address_city.selection = -1
                address_kecamatan.selection = -1
                address_kelurahan.selection = -1

                memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { member ->
                    coroutineScope.launch(Dispatchers.Main) {
                        if(provinces.isNotEmpty()) {
                            val selectedProvince = provinces[position]
                            kabupatens = offlineViewModel.getKabupatenByProvinceId(selectedProvince.id)
                            cityAdapter.clear()
                            cityAdapter.addAll(kabupatens)
                            cityAdapter.notifyDataSetChanged()

                            val kabupaten = offlineViewModel.getKabupatenByNameAndProvinceId(member.kotaKantor, selectedProvince.id)
                            val pos = cityAdapter.getPosition(kabupaten)
                            address_city.selection = if (member.kotaKantorPosisi >= 0) member.kotaKantorPosisi else if (pos >= 0) pos else -1
                        }
                    }
                })
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        address_city.onItemSelectedListener = object: MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                address_kecamatan.selection = -1
                address_kelurahan.selection = -1

                memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { member ->
                    coroutineScope.launch(Dispatchers.Main) {
                        if(kabupatens.isNotEmpty()) {
                            val selectedKabupaten = kabupatens[position]
                            kecamatans = offlineViewModel.getKecamatanByKabupatenId(selectedKabupaten.id)
                            kecamatanAdapter.clear()
                            kecamatanAdapter.addAll(kecamatans)
                            kecamatanAdapter.notifyDataSetChanged()

                            val kecamatan = offlineViewModel.getKecamatanByNameAndKabupatenId(member.kecamatanKantor, selectedKabupaten.id)
                            val pos = kecamatanAdapter.getPosition(kecamatan)
                            address_kecamatan.selection = if (member.kecamatanKantorPosisi >= 0) member.kecamatanKantorPosisi else if (pos >= 0) pos else -1
                        }
                    }
                })
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        address_kecamatan.onItemSelectedListener = object: MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                address_kelurahan.selection = -1
                memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { member ->
                    coroutineScope.launch(Dispatchers.Main) {
                        if(kecamatans.isNotEmpty()) {
                            val selectedKecamatan = kecamatans[position]
                            kelurahans = offlineViewModel.getDesaByKecamatanId(selectedKecamatan.id)
                            kelurahanAdapter.clear()
                            kelurahanAdapter.addAll(kelurahans)
                            kelurahanAdapter.notifyDataSetChanged()

                            val kelurahan = offlineViewModel.getDesaByNameAndKecamatanId(member.kelurahanKantor, selectedKecamatan.id)
                            val pos = kelurahanAdapter.getPosition(kelurahan)
                            address_kelurahan.selection = if (member.kelurahanKantorPosisi >= 0) member.kelurahanKantorPosisi else if (pos >= 0) pos else -1
                        }
                    }
                })
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            npwp_exist.selection = member.memilikiNpwp
            npwp_no.setText(member.nomorNpwp)
            occupation_status.selection = member.pekerjaan
//            occupation_field.setText(member.bidangPekerjaan)
//            occupation_position.setText(member.posisiPekerjaan)
            jenis_umkm.setText(member.jenisUmkm)
            company_name.setText(member.namaPerusahaan)
            work_how_long_month.setSelection(member.lamaBulanBekerja + 1)
            work_how_long_year.setSelection(member.lamaTahunBekerja + 1)
            occupation_revenue.setText(CurrencyFormat.formatRupiah.format(member.penghasilan).toString())
            address_street.setText(member.jalanKantor)
//            address_no.setText(member.nomorKantor)
//            address_rt.setText(member.rtKantor)
//            address_rw.setText(member.rwKantor)
            address_province.selection = member.provinsiKantorPosisi

            var province: Province
            coroutineScope.launch(Dispatchers.Main) {
                province = offlineViewModel.getProvinceByName(member.provinsiKantor)
                var position = provinceAdapter.getPosition(province)
                Log.d("OccupationFragment", "Posisi Spinner Provinsi Kantor : ${member.provinsiKantorPosisi}")
                Log.d("OccupationFragment", "Provinsi Kantor : ${member.provinsiKantor}")
                Log.d("OccupationFragment", "Posisi Provinsi Kantor : $position")
                address_province.selection = if (member.provinsiKantorPosisi >= 0) member.provinsiKantorPosisi else if (position >= 0) position else -1
            }

            address_kode_pos.setText(member.kodePosKantor.toString())

//            address_city.selection = member.kotaKantorPosisi
//            address_kecamatan.selection = member.kecamatanKantorPosisi
//            address_kelurahan.selection = member.kelurahanKantorPosisi
        })

    }

    private fun onBackButtonClicked(view: View) {
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

        setMember(member)

        val fragment = AddressFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
//        activity?.onBackPressed()
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
//        if(configPreferences.getInt("bidang_pekerja", 1) == 1 && occupation_field.text.toString().isEmpty()) {
//            occupation_field_container.error = "Bidang Pekerjaan/Usaha tidak boleh kosong"
//            allTrue = false
//        }
        if(configPreferences.getInt("jenis_umkm", 1) == 1 && configPreferences.getInt("pekerja_usaha", 1) == 6 && jenis_umkm.text.toString().isEmpty()) {
            jenis_umkm_container.error = "Jenis UMKM tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("nama_perusahaan", 1) == 1 && company_name.text.toString().isEmpty()) {
            company_name_container.error = "Nama Perusahaan/Usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("lama_bekerja", 1) == 1) {
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
        if(configPreferences.getInt("penghasilan_omset", 1) == 1 && (occupation_revenue.text.toString().isEmpty() || occupation_revenue.text.toString() == "Rp0")) {
            occupation_revenue_container.error = "Penghasilan/Omset usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_jalan", 1) == 1 && address_street.text.toString().isEmpty()) {
            address_street_container.error = "Alamat Kantor : Jalan tidak boleh kosong"
            allTrue = false
        }
//        if(configPreferences.getInt("alamat_kantor_nomer", 1) == 1 && address_no.text.toString().isEmpty()) {
//            address_no_container.error = "Alamat Kantor : No tidak boleh kosong"
//            allTrue = false
//        }
//        if(configPreferences.getInt("alamat_kantor_rt", 1) == 1 && address_rt.text.toString().isEmpty()) {
//            address_rt_container.error = "Alamat Kantor : RT tidak boleh kosong"
//            allTrue = false
//        }
        if(configPreferences.getInt("alamat_kantor_kelurahan", 1) == 1 && address_kelurahan.selectedItemId < 0) {
            address_kelurahan.error = "Alamat Kantor : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_kecamatan", 1) == 1 && address_kecamatan.selectedItemId < 0) {
            address_kecamatan.error = "Alamat Kantor : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_kota", 1) == 1 && address_city.selectedItemId < 0) {
            address_city.error = "Alamat Kantor : Kota tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_provinsi", 1) == 1 && address_province.selectedItemId < 0) {
            address_province.error = "Alamat Kantor : Provinsi tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_kantor_kode_pos", 1) == 1 && address_kode_pos.text.toString().isEmpty()) {
            address_kode_pos_container.error = "Alamat Kantor : Kode Pos tidak boleh kosong"
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

            setMember(member)
            val fragment = EmergencyContactFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.member_acquisition_fragment_container, fragment).addToBackStack(null).commit()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
        }
    }

    private fun setMember(member: Member) {
        member.memilikiNpwp = npwp_exist.selection
        member.nomorNpwp = npwp_no.text.toString()
        member.pekerjaan = occupation_status.selection
//        member.bidangPekerjaan = occupation_field.text.toString()
//        member.posisiPekerjaan = occupation_position.text.toString()
        member.jenisUmkm = jenis_umkm.text.toString()
        member.namaPerusahaan = company_name.text.toString()
        member.lamaBulanBekerja = work_how_long_month.selectedItemPosition - 1
        member.lamaTahunBekerja = work_how_long_year.selectedItemPosition - 1
        member.penghasilan = CurrencyFormat.removeCurrencyFormat(occupation_revenue.text.toString()).toLong()
        member.jalanKantor = address_street.text.toString()
//        member.nomorKantor = address_no.text.toString()
//        member.rtKantor = address_rt.text.toString()
//        member.rwKantor = address_rw.text.toString()
        member.kelurahanKantorPosisi = address_kelurahan.selection
        member.kecamatanKantorPosisi = address_kecamatan.selection
        member.kotaKantorPosisi = address_city.selection
        member.provinsiKantorPosisi = address_province.selection
        member.kodePosKantor = address_kode_pos.text.toString().toInt()
        if(address_kelurahan.selectedItem != null) {
            member.kelurahanKantor = (address_kelurahan.selectedItem as Desa).name
        }
        if(address_kecamatan.selectedItem != null) {
            member.kecamatanKantor = (address_kecamatan.selectedItem as Kecamatan).name
        } else {
            member.kecamatanKantor = ""
        }
        if(address_city.selectedItem != null) {
            member.kotaKantor = (address_city.selectedItem as Kabupaten).name
        } else {
            member.kotaKantor = ""
        }
        if(address_province.selectedItem != null) {
            member.provinsiKantor = (address_province.selectedItem as Province).name
        } else {
            member.provinsiKantor = ""
        }

        memberViewModel.setMember(member)
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
