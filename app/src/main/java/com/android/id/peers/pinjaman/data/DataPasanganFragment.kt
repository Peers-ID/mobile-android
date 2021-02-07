package com.android.id.peers.pinjaman.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.communication.MemberViewModel
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.button_bottom.*
import kotlinx.android.synthetic.main.fragment_pasangan.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Use the [DataPasanganFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataPasanganFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_pasangan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PasanganFragment", " NO HP : ${memberViewModel.member.value!!.noHp}")
        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("nama_pasangan", 1) == 0) {
            nama_pasangan.visibility = View.GONE
            nama_pasangan_container.visibility = View.GONE
        }
        if (configPreferences.getInt("no_identitas_pasangan", 1) == 0) {
            no_id_pasangan.visibility = View.GONE
            no_id_pasangan_container.visibility = View.GONE
        }
        if (configPreferences.getInt("pekerjaan_pasangan", 1) == 0) {
            pekerjaan_pasangan.visibility = View.GONE
            pekerjaan_pasangan_container.visibility = View.GONE
        }
        if (configPreferences.getInt("no_hp_pasangan", 1) == 0) {
            no_hp_pasangan.visibility = View.GONE
            no_hp_pasangan_container.visibility = View.GONE
        }

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            nama_pasangan.setText(member.namaPasangan)
            no_id_pasangan.setText(member.noIdentitasPasangan)
            pekerjaan_pasangan.setText(member.pekerjaanPasangan)
            no_hp_pasangan.setText(member.noHpPasangan)
        })
    }

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(3, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        member.namaPasangan = nama_pasangan.text.toString()
        member.noIdentitasPasangan = no_id_pasangan.text.toString()
        member.pekerjaanPasangan = pekerjaan_pasangan.text.toString()
        member.noHpPasangan = no_hp_pasangan.text.toString()

        memberViewModel.setMember(member)

        val fragment = EmergencyContactFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
//        activity!!.onBackPressed()
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("nama_pasangan", 1) == 1 && nama_pasangan.text.toString().isEmpty()) {
            nama_pasangan_container.error = "Nama Pasangan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_identitas_pasangan", 1) == 1 && no_id_pasangan.text.toString().isEmpty()) {
            no_id_pasangan_container.error = "No ID Pasangan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("pekerjaan_pasangan", 1) == 1 && pekerjaan_pasangan.text.toString().isEmpty()) {
            pekerjaan_pasangan_container.error = "Pekerjaan Pasangan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_hp_pasangan", 1) == 1 && no_hp_pasangan.text.toString().isEmpty()) {
            no_hp_pasangan_container.error = "No Handphone Pasangan tidak boleh kosong"
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

            member.namaPasangan = nama_pasangan.text.toString()
            member.noIdentitasPasangan = no_id_pasangan.text.toString()
            member.pekerjaanPasangan = pekerjaan_pasangan.text.toString()
            member.noHpPasangan = no_hp_pasangan.text.toString()

            memberViewModel.setMember(member)

            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(5, true)

            val fragment = DataPenjaminFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()

//            val intent = Intent(activity, MemberAcquisitionConfirmationActivity::class.java)
//            intent.putExtra("member", memberViewModel.member.value)
//            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PasanganFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataPasanganFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}