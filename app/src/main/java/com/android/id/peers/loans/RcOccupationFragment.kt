package com.android.id.peers.loans

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
import kotlinx.android.synthetic.main.fragment_rc_occupation.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Use the [RcOccupationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RcOccupationFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_rc_occupation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { member ->
            npwp_exist.text = member.memilikiNpwpString()
            npwp_no.text = member.nomorNpwp
            occupation.text = member.pekerjaanString()
            occupation_field.text = member.bidangPekerjaan
            occupation_position.text = member.posisiPekerjaan
            company_name.text = member.namaPerusahaan
            val lamaKerja = "${member.lamaBulanBekerja} bulan / ${member.lamaTahunBekerja} tahun"
            work_how_long.text = lamaKerja
            occupation_revenue.text = member.penghasilan.toString()

            office_address_street.text = member.jalanKantor
            office_address_no.text = member.nomorKantor
            office_address_rt.text = member.rtKantor
            office_address_rw.text = member.rwKantor
            office_address_kelurahan.text = member.kelurahanKantor
            office_address_kecamatan.text = member.kecamatanKantor
            office_address_city.text = member.kotaKantor
            office_address_province.text = member.provinsiKantor
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RcOccupationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RcOccupationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
