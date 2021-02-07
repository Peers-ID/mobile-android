package com.android.id.peers.pinjaman.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
import com.android.id.peers.members.models.Member
import com.android.id.peers.pinjaman.pengajuan.PengajuanPinjamanActivity
import com.android.id.peers.util.communication.MemberViewModel
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.button_bottom.*
import kotlinx.android.synthetic.main.fragment_dokumen.*
import kotlinx.android.synthetic.main.fragment_occupation.*
import kotlinx.android.synthetic.main.fragment_survey.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Use the [SurveyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SurveyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_survey, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SurveyFragment", " NO HP : ${memberViewModel.member.value!!.noHp}")
        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        val luasRumahAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.luas_rumah))
        luasRumahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        luas_rumah.adapter = luasRumahAdapter

        setAdapter(R.id.luas_rumah, R.array.luas_rumah)
        setAdapter(R.id.jenis_atap, R.array.jenis_atap)
        setAdapter(R.id.jenis_dinding, R.array.jenis_dinding)
        setAdapter(R.id.kondisi_rumah, R.array.kondisi_rumah)
        setAdapter(R.id.letak_rumah, R.array.letak_rumah)
        setAdapter(R.id.data_fisik_perabot, R.array.fisik_perabot_elektronik)
        setAdapter(R.id.akses_lembaga_keuangan, R.array.akses_lembaga_keuangan)
        setAdapter(R.id.info_ttg_usaha, R.array.info_ttg_usaha)
        setAdapter(R.id.indeks_asset, R.array.indeks_asset)
        setAdapter(R.id.indeks_rumah, R.array.indeks_rumah)
        setAdapter(R.id.kepemilikan_asset, R.array.kepemilikan_asset)
        setAdapter(R.id.pendapatan_luar_usaha, R.array.pendapatan_luar_usaha)
        setAdapter(R.id.perkembangan_asset, R.array.perkembangan_asset)
        setAdapter(R.id.perkembangan_usaha, R.array.perkembangan_usaha)

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("survey_luas_rumah", 1) == 0) {
            luas_rumah.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_jenis_atap", 1) == 0) {
            jenis_atap.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_jenis_dinding", 1) == 0) {
            jenis_dinding.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_kondisi_rumah", 1) == 0) {
            kondisi_rumah.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_letak_rumah", 1) == 0) {
            letak_rumah.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_tanggungan_keluarga", 1) == 0) {
            tanggungan_keluarga_container.visibility = View.GONE
            tanggungan_keluarga.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_data_fisik_perabot", 1) == 0) {
            data_fisik_perabot.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_akses_lembaga_keuangan", 1) == 0) {
            akses_lembaga_keuangan.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_info_ttg_usaha", 1) == 0) {
            info_ttg_usaha.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_index_rumah", 1) == 0) {
            indeks_rumah.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_index_asset", 1) == 0) {
            indeks_asset.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_kepemilikan_asset", 1) == 0) {
            kepemilikan_asset.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_pendapatan_luar_usaha", 1) == 0) {
            pendapatan_luar_usaha.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_perkembangan_asset", 1) == 0) {
            perkembangan_asset.visibility = View.GONE
        }
        if(configPreferences.getInt("survey_perkembangan_usaha", 1) == 0) {
            perkembangan_usaha.visibility = View.GONE
        }

//        anggotaContext = context!!

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { member ->
            letak_rumah.selection = member.surveyLetakRumah
            jenis_atap.selection = member.surveyJenisAtap
            jenis_dinding.selection = member.surveyJenisDinding
            luas_rumah.selection = member.surveyLuasRumah
            kondisi_rumah.selection = member.surveyKondisiRumah
            tanggungan_keluarga.setText(member.surveyTanggunganKeluarga)
            data_fisik_perabot.selection = member.surveyDataFisikPerabot
            akses_lembaga_keuangan.selection = member.surveyAksesLembagaKeuangan
            info_ttg_usaha.selection = member.surveyInfoTtgUsaha
            indeks_asset.selection = member.surveyIndexAsset
            indeks_rumah.selection = member.surveyIndexRumah
            kepemilikan_asset.selection = member.surveyKepemilikanAsset
            pendapatan_luar_usaha.selection = member.surveyPendapatanLuarUsaha
            perkembangan_usaha.selection = member.surveyPerkembanganUsaha
            perkembangan_asset.selection = member.surveyPerkembanganAsset
        })
    }



    private fun setAdapter(layoutId: Int, stringArrayResourceId: Int) {
        val adapter = ArrayAdapter<String>(view!!.context, android.R.layout.simple_spinner_item, resources.getStringArray(stringArrayResourceId))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activity!!.findViewById<MaterialSpinner>(layoutId).adapter = adapter
    }

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(6, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        setMember(member)

        val fragment = OccupationFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
//        activity!!.onBackPressed()
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("survey_luas_rumah", 1) == 1 && luas_rumah.selection < 0) {
            luas_rumah.error = "Luas Rumah tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_jenis_atap", 1) == 1 && jenis_atap.selection < 0) {
            jenis_atap.error = "Jenis Atap tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_jenis_dinding", 1) == 1 && jenis_dinding.selection < 0) {
            jenis_dinding.error = "Jenis Dinding tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_kondisi_rumah", 1) == 1 && kondisi_rumah.selection < 0) {
            kondisi_rumah.error = "Kondisi Rumah tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_letak_rumah", 1) == 1 && letak_rumah.selection < 0) {
            letak_rumah.error = "Letak Rumah tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_tanggungan_keluarga", 1) == 1 && tanggungan_keluarga.text.toString().isEmpty()) {
            tanggungan_keluarga.error = "Tanggungan Keluarga tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_data_fisik_perabot", 1) == 1 && data_fisik_perabot.selection < 0) {
            data_fisik_perabot.error = "Data Fisik Perabot tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_akses_lembaga_keuangan", 1) == 1 && akses_lembaga_keuangan.selection < 0) {
            akses_lembaga_keuangan.error = "Akses Lembaga Keuangan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_info_ttg_usaha", 1) == 1 && info_ttg_usaha.selection < 0) {
            info_ttg_usaha.error = "Info Tentang Usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_index_rumah", 1) == 1 && indeks_rumah.selection < 0) {
            indeks_rumah.error = "Indeks Rumah tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_index_asset", 1) == 1 && indeks_asset.selection < 0) {
            indeks_asset.error = "Indeks Asset tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_kepemilikan_asset", 1) == 1 && kepemilikan_asset.selection < 0) {
            kepemilikan_asset.error = "Kepemilikan Asset tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_pendapatan_luar_usaha", 1) == 1 && pendapatan_luar_usaha.selection < 0) {
            pendapatan_luar_usaha.error = "Pendapatan Luar Usaha tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_perkembangan_asset", 1) == 1 && perkembangan_asset.selection < 0) {
            perkembangan_asset.error = "Perkembangan Asset tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("survey_perkembangan_usaha", 1) == 1 && perkembangan_usaha.selection < 0) {
            perkembangan_usaha.error = "Perkembangan Usaha tidak boleh kosong"
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

            setMember(member)

            val fragment = OccupationFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()

            val intent = Intent(activity, PengajuanPinjamanActivity::class.java)
            Log.d("SurveyFragment", "No HP Member : ${memberViewModel.member.value!!.noHp}")
            intent.putExtra("member", memberViewModel.member.value)
            startActivity(intent)
        }
    }

    private fun setMember(member: Member) {
        member.surveyLetakRumah = letak_rumah.selection
        member.surveyJenisAtap = jenis_atap.selection
        member.surveyJenisDinding = jenis_dinding.selection
        member.surveyLuasRumah = luas_rumah.selection
        member.surveyKondisiRumah = kondisi_rumah.selection
        member.surveyTanggunganKeluarga = tanggungan_keluarga.text.toString()
        member.surveyDataFisikPerabot = data_fisik_perabot.selection
        member.surveyAksesLembagaKeuangan = akses_lembaga_keuangan.selection
        member.surveyInfoTtgUsaha = info_ttg_usaha.selection
        member.surveyIndexAsset = indeks_asset.selection
        member.surveyIndexRumah = indeks_rumah.selection
        member.surveyKepemilikanAsset = kepemilikan_asset.selection
        member.surveyPendapatanLuarUsaha = pendapatan_luar_usaha.selection
        member.surveyPerkembanganUsaha = perkembangan_usaha.selection
        member.surveyPerkembanganAsset = perkembangan_asset.selection

        memberViewModel.setMember(member)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SurveyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SurveyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}