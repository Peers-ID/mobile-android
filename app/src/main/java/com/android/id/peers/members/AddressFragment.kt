package com.android.id.peers.members

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.*
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.util.database.OfflineViewModel
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_address.*
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
 * [AddressFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddressFragment : Fragment(), CoroutineScope {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var offlineViewModel: OfflineViewModel
    private var provinces: List<Province> = ArrayList()
    private var kabupatens: List<Kabupaten> = ArrayList()
    private var kecamatans: List<Kecamatan> = ArrayList()
    private var kelurahans: List<Desa> = ArrayList()
    private var kabupatensDomisili: List<Kabupaten> = ArrayList()
    private var kecamatansDomisili: List<Kecamatan> = ArrayList()
    private var kelurahansDomisili: List<Desa> = ArrayList()
    private val coroutineScope = this

    private lateinit var provinceAdapter: ArrayAdapter<Province>
//            val arrayAdapter = RegionAdapter(context!!, provinces, "Province")
    private lateinit var cityAdapter: ArrayAdapter<Kabupaten>
    private lateinit var cityDomisiliAdapter: ArrayAdapter<Kabupaten>
    private lateinit var kecamatanAdapter: ArrayAdapter<Kecamatan>
    private lateinit var kecamatanDomisiliAdapter: ArrayAdapter<Kecamatan>
    private lateinit var kelurahanAdapter: ArrayAdapter<Desa>
    private lateinit var kelurahanDomisiliAdapter: ArrayAdapter<Desa>

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + SupervisorJob()

    override fun onDestroy() {
        super.onDestroy()
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
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("alamat_ktp_jalan", 1) == 0) {
            address_street.visibility = View.GONE
            address_street_container.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_ktp_nomer", 1) == 0) {
            address_no.visibility = View.GONE
            address_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_ktp_rt", 1) == 0) {
            address_rt.visibility = View.GONE
            address_rt_container.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_ktp_rw", 1) == 0) {
            address_rw.visibility = View.GONE
            address_rw_container.visibility = View.GONE
        }
//        if (configPreferences.getInt("alamat_ktp_kelurahan", 1) == 0) {
//            address_kelurahan.visibility = View.GONE
//            address_kelurahan_container.visibility = View.GONE
//        }
        if (configPreferences.getInt("alamat_ktp_kelurahan", 1) == 0) address_kelurahan.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_kecamatan", 1) == 0) address_kecamatan.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_status_tempat_tinggal", 1) == 0) address_status.visibility = View.GONE
        if (configPreferences.getInt("alamat_ktp_lama_tinggal", 1) == 0) {
            how_long_text.visibility = View.GONE
            address_how_long_month.visibility = View.GONE
            address_how_long_year.visibility = View.GONE
        }
        if (configPreferences.getInt("domisili_sesuai_ktp", 1) == 0) address_domisili_equal_ktp.visibility = View.GONE
        if (configPreferences.getInt("alamat_domisili_jalan", 1) == 0) {
            address_street_domisili.visibility = View.GONE
            address_street_domisili_container.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_domisili_nomer", 1) == 0) {
            address_no_domisili.visibility = View.GONE
            address_no_domisili_container.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_domisili_rt", 1) == 0) {
            address_rt_domisili.visibility = View.GONE
            address_rt_domisili_container.visibility = View.GONE
        }
        if (configPreferences.getInt("alamat_domisili_rw", 1) == 0) {
            address_rw_domisili.visibility = View.GONE
            address_rw_domisili_container.visibility = View.GONE
        }
//        if (configPreferences.getInt("alamat_domisili_kelurahan", 1) == 0) {
//            address_kelurahan_domisili.visibility = View.GONE
//            address_kelurahan_domisili_container.visibility = View.GONE
//        }
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

        provinceAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, provinces)
//            val arrayAdapter = RegionAdapter(context!!, provinces, "Province")
        cityAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kabupatens)
        cityDomisiliAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kabupatensDomisili)
        kecamatanAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kecamatans)
        kecamatanDomisiliAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kecamatansDomisili)
        kelurahanAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kelurahans)
        kelurahanDomisiliAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, kelurahansDomisili)

        val addressStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.address_status))
        addressStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        address_status.adapter = addressStatusAdapter

        val addressStatusDomisiliAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.address_status))
        addressStatusDomisiliAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        address_status_domisili.adapter = addressStatusDomisiliAdapter
        /* Member View Model */

        address_province.adapter = provinceAdapter
        address_province_domisili.adapter = provinceAdapter

        address_city.adapter = cityAdapter
        address_city_domisili.adapter = cityDomisiliAdapter
        address_kecamatan.adapter = kecamatanAdapter
        address_kecamatan_domisili.adapter = kecamatanDomisiliAdapter
        address_kelurahan.adapter = kelurahanAdapter
        address_kelurahan_domisili.adapter = kelurahanDomisiliAdapter

        offlineViewModel.allProvince.observe(viewLifecycleOwner, Observer {provinces ->
            this.provinces = provinces
            provinceAdapter.clear()
            provinceAdapter.addAll(provinces)
            provinceAdapter.notifyDataSetChanged()
        })

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
                coroutineScope.launch(Dispatchers.Main) {
                    if(provinces.isNotEmpty()) {
                        kabupatens = offlineViewModel.getKabupatenByProvinceId(provinces[position].id)
                        cityAdapter.clear()
                        cityAdapter.addAll(kabupatens)
                        cityAdapter.notifyDataSetChanged()
                    }
                }
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
                coroutineScope.launch(Dispatchers.Main) {
                    if(kabupatens.isNotEmpty()) {
                        kecamatans = offlineViewModel.getKecamatanByKabupatenId(kabupatens[position].id)
                        kecamatanAdapter.clear()
                        kecamatanAdapter.addAll(kecamatans)
                        kecamatanAdapter.notifyDataSetChanged()
                    }
                }
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
                coroutineScope.launch(Dispatchers.Main) {
                    if(kecamatans.isNotEmpty()) {
                        kelurahans = offlineViewModel.getDesaByKecamatanId(kecamatans[position].id)
                        kelurahanAdapter.clear()
                        kelurahanAdapter.addAll(kelurahans)
                        kelurahanAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        address_province_domisili.onItemSelectedListener = object: MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                address_city_domisili.selection = -1
                address_kecamatan_domisili.selection = -1
                address_kelurahan_domisili.selection = -1
                coroutineScope.launch(Dispatchers.Main) {
                    if(provinces.isNotEmpty()) {
                        kabupatensDomisili = offlineViewModel.getKabupatenByProvinceId(provinces[position].id)
                        cityDomisiliAdapter.clear()
                        cityDomisiliAdapter.addAll(kabupatensDomisili)
                        cityDomisiliAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        address_city_domisili.onItemSelectedListener = object: MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                address_kecamatan_domisili.selection = -1
                address_kelurahan_domisili.selection = -1
                coroutineScope.launch(Dispatchers.Main) {
                    if(kabupatensDomisili.isNotEmpty()) {
                        kecamatansDomisili = offlineViewModel.getKecamatanByKabupatenId(kabupatensDomisili[position].id)
                        kecamatanDomisiliAdapter.clear()
                        kecamatanDomisiliAdapter.addAll(kecamatansDomisili)
                        kecamatanDomisiliAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        address_kecamatan_domisili.onItemSelectedListener = object: MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                address_kelurahan_domisili.selection = -1
                coroutineScope.launch(Dispatchers.Main) {
                    if(kecamatansDomisili.isNotEmpty()) {
                        kelurahansDomisili = offlineViewModel.getDesaByKecamatanId(kecamatansDomisili[position].id)
                        kelurahanDomisiliAdapter.clear()
                        kelurahanDomisiliAdapter.addAll(kelurahansDomisili)
                        kelurahanDomisiliAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }

        }

        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            address_street.setText(member.jalanSesuaiKtp)
            address_no.setText(member.nomorSesuaiKtp)
            address_rt.setText(member.rtSesuaiKtp)
            address_rw.setText(member.rwSesuaiKtp)
            address_province.selection = member.provinsiSesuaiKtpPosisi
            address_city.selection = member.kotaSesuaiKtpPosisi
            address_kecamatan.selection = member.kecamatanSesuaiKtpPosisi
            address_kelurahan.selection = member.kelurahanSesuaiKtpPosisi

            address_status.selection = member.statusTempatTinggalSesuaiKtp
            address_how_long_month.setSelection(member.lamaBulanTinggalSesuaiKtp)
            address_how_long_year.setSelection(member.lamaTahunTinggalSesuaiKtp)
            address_domisili_equal_ktp.isChecked = member.domisiliSesuaiKtp
            address_street_domisili.setText(member.jalanDomisili)
            address_no_domisili.setText(member.nomorDomisili)
            address_rt_domisili.setText(member.rtDomisili)
            address_rw_domisili.setText(member.rwDomisili)
            address_province_domisili.selection = member.provinsiDomisiliPosisi
            address_city_domisili.selection = member.kotaDomisiliPosisi
            address_kecamatan_domisili.selection = member.kecamatanDomisiliPosisi
            address_kelurahan_domisili.selection = member.kelurahanDomisiliPosisi
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

    fun onProvinceSelected() {

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
        member.kelurahanSesuaiKtpPosisi = address_kelurahan.selection
        member.kecamatanSesuaiKtpPosisi = address_kecamatan.selection
        member.kotaSesuaiKtpPosisi = address_city.selection
        member.provinsiSesuaiKtpPosisi = address_province.selection
        if(address_kecamatan.selectedItem != null) {
            member.kecamatanSesuaiKtp = (address_kecamatan.selectedItem as Kecamatan).name
        } else {
            member.kecamatanSesuaiKtp = ""
        }
        if(address_city.selectedItem != null) {
            member.kotaSesuaiKtp = (address_city.selectedItem as Kabupaten).name
        } else {
            member.kotaSesuaiKtp = ""
        }
        if(address_province.selectedItem != null) {
            member.provinsiSesuaiKtp = (address_province.selectedItem as Province).name
        } else {
            member.provinsiSesuaiKtp = ""
        }
        member.statusTempatTinggalSesuaiKtp = address_status.selection
        member.lamaBulanTinggalSesuaiKtp = address_how_long_month.selectedItemPosition
        member.lamaTahunTinggalSesuaiKtp = address_how_long_year.selectedItemPosition
        member.domisiliSesuaiKtp = address_domisili_equal_ktp.isChecked
        member.jalanDomisili = address_street_domisili.text.toString()
        member.nomorDomisili = address_no_domisili.text.toString()
        member.rtDomisili = address_rt_domisili.text.toString()
        member.rwDomisili = address_rw_domisili.text.toString()
        member.kelurahanDomisiliPosisi = address_kelurahan_domisili.selection
        member.kecamatanDomisiliPosisi = address_kecamatan_domisili.selection
        member.kotaDomisiliPosisi = address_city_domisili.selection
        member.provinsiDomisiliPosisi = address_province_domisili.selection
        if(address_kecamatan_domisili.selectedItem != null) {
            member.kecamatanDomisili = (address_kecamatan_domisili.selectedItem as Kecamatan).name
        } else {
            member.kecamatanDomisili = ""
        }
        if(address_city_domisili.selectedItem != null) {
            member.kotaDomisili = (address_city_domisili.selectedItem as Kabupaten).name
        } else {
            member.kotaDomisili = ""
        }
        if(address_province_domisili.selectedItem != null) {
            member.provinsiDomisili = (address_province_domisili.selectedItem as Province).name
        } else {
            member.provinsiDomisili = ""
        }
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
//        if(configPreferences.getInt("alamat_ktp_kelurahan", 1) == 1 && address_kelurahan.text.toString().isEmpty()) {
//            address_kelurahan_container.error = "Alamat sesuai KTP : Kelurahan tidak boleh kosong"
//            allTrue = false
//        }
//        if(configPreferences.getInt("alamat_ktp_kecamatan", 1) == 1 && address_kecamatan.text.toString().isEmpty()) {
//            address_kecamatan_container.error = "Alamat sesuai KTP : Kecamatan tidak boleh kosong"
//            allTrue = false
//        }
        if(configPreferences.getInt("alamat_ktp_kelurahan", 1) == 1 && address_kelurahan.selectedItemId < 0) {
            address_kelurahan.error = "Alamat sesuai KTP : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_kecamatan", 1) == 1 && address_kecamatan.selectedItemId < 0) {
            address_kecamatan.error = "Alamat sesuai KTP : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_kota", 1) == 1 && address_city.selectedItemId < 0) {
            address_city.error = "Alamat sesuai KTP : Kota tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_ktp_provinsi", 1) == 1 && address_province.selectedItemId < 0) {
            address_province.error = "Alamat sesuai KTP : Provinsi tidak boleh kosong"
            allTrue = false
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
//        if(configPreferences.getInt("alamat_domisili_kelurahan", 1) == 1 && address_kelurahan_domisili.text.toString().isEmpty()) {
//            address_kelurahan_domisili_container.error = "Alamat domisili : Kelurahan tidak boleh kosong"
//            allTrue = false
//        }
        if(configPreferences.getInt("alamat_domisili_kelurahan", 1) == 1 && address_kelurahan_domisili.selectedItemId < 0) {
            address_kelurahan_domisili.error = "Alamat domisili : Kelurahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_kecamatan", 1) == 1 && address_kecamatan_domisili.selectedItemId < 0) {
            address_kecamatan_domisili.error = "Alamat domisili : Kecamatan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_kota", 1) == 1 && address_city_domisili.selectedItemId < 0) {
            address_city_domisili.error = "Alamat domisili : Kota tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("alamat_domisili_provinsi", 1) == 1 && address_province_domisili.selectedItemId < 0) {
            address_province_domisili.error = "Alamat domisili : Provinsi tidak boleh kosong"
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
            address_province_domisili.selection = address_province.selection

            kabupatensDomisili = kabupatens
            cityDomisiliAdapter.clear()
            cityDomisiliAdapter.addAll(kabupatensDomisili)
            cityDomisiliAdapter.notifyDataSetChanged()
            address_city_domisili.selection = address_city.selection

            kecamatansDomisili = kecamatans
            kecamatanDomisiliAdapter.clear()
            kecamatanDomisiliAdapter.addAll(kecamatansDomisili)
            kecamatanDomisiliAdapter.notifyDataSetChanged()
            address_kecamatan_domisili.selection = address_kecamatan.selection

            kelurahansDomisili = kelurahans
            kelurahanDomisiliAdapter.clear()
            kelurahanDomisiliAdapter.addAll(kelurahansDomisili)
            kelurahanDomisiliAdapter.notifyDataSetChanged()
            address_kelurahan_domisili.selection = address_kelurahan.selection

            address_status_domisili.selection = address_status.selection
            address_how_long_domisili_month.setSelection(address_how_long_month.selectedItemPosition)
            address_how_long_domisili_year.setSelection(address_how_long_year.selectedItemPosition)
        }else{
            address_street_domisili.setText("")
            address_no_domisili.setText("")
            address_rt_domisili.setText("")
            address_rw_domisili.setText("")
            address_province_domisili.selection = 0
            address_city_domisili.selection = 0
            address_kecamatan_domisili.selection = 0
            address_kelurahan_domisili.selection = 0
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
