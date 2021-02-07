package com.android.id.peers.loans_unused

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.communication.MemberViewModel
import kotlinx.android.synthetic.main.fragment_rc_address.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Use the [RcAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RcAddressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberViewModel = ViewModelProvider(activity!!).get(MemberViewModel::class.java)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rc_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { member ->
            address_street.text = member.jalanSesuaiKtp
//            address_no.text = member.nomorSesuaiKtp
//            address_rt.text = member.rtSesuaiKtp
//            address_rw.text = member.rwSesuaiKtp
            address_kelurahan.text = member.kelurahanSesuaiKtp
            address_kecamatan.text = member.kecamatanSesuaiKtp
            address_city.text = member.kotaSesuaiKtp
            address_province.text = member.provinsiSesuaiKtp
            address_status.text = member.statusTempatTinggalSesuaiKtpString()
            val lamaTinggalKtp = "${member.lamaBulanTinggalSesuaiKtp} bulan / ${member.lamaTahunTinggalSesuaiKtp} tahun"
            address_how_long.text = lamaTinggalKtp

            address_street_domisili.text = member.jalanDomisili
//            address_no_domisili.text = member.nomorDomisili
//            address_rt_domisili.text = member.rtDomisili
//            address_rw_domisili.text = member.rwDomisili
            address_kelurahan_domisili.text = member.kelurahanDomisili
            address_kecamatan_domisili.text = member.kecamatanDomisili
            address_city_domisili.text = member.kotaDomisili
            address_province_domisili.text = member.provinsiDomisili
            address_status_domisili.text = member.statusTempatTinggalDomisiliString()
            val lamaTinggalDomisili = "${member.lamaBulanTinggalDomisili} bulan / ${member.lamaTahunTinggalDomisili} tahun"
            address_how_long_domisili.text = lamaTinggalDomisili
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RcAddressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RcAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
