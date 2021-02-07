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
import kotlinx.android.synthetic.main.fragment_data_penjamin.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Use the [DataPenjaminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataPenjaminFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_data_penjamin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("PenjaminFragment", " NO HP : ${memberViewModel.member.value!!.noHp}")
        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("nama_penjamin", 1) == 0) {
            nama_penjamin.visibility = View.GONE
            nama_penjamin_container.visibility = View.GONE
        }
        if (configPreferences.getInt("hubungan_penjamin", 1) == 0) {
            hubungan_penjamin.visibility = View.GONE
            hubungan_penjamin_container.visibility = View.GONE
        }
        if (configPreferences.getInt("no_hp_penjamin", 1) == 0) {
            no_hp_penjamin.visibility = View.GONE
            no_hp_penjamin_container.visibility = View.GONE
        }

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->
            nama_penjamin.setText(member.namaPenjamin)
            hubungan_penjamin.setText(member.hubunganPenjamin)
            no_hp_penjamin.setText(member.noHpPenjamin)
        })
    }

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(4, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        member.namaPenjamin = nama_penjamin.text.toString()
        member.hubunganPenjamin = hubungan_penjamin.text.toString()
        member.noHpPenjamin = no_hp_penjamin.text.toString()

        memberViewModel.setMember(member)

        val fragment = DataPasanganFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
//        activity!!.onBackPressed()
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if(configPreferences.getInt("nama_penjamin", 1) == 1 && nama_penjamin.text.toString().isEmpty()) {
            nama_penjamin_container.error = "Nama penjamin tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_hp_penjamin", 1) == 1 && no_hp_penjamin.text.toString().isEmpty()) {
            no_hp_penjamin_container.error = "No Handphone penjamin tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("hubungan_penjamin", 1) == 1 && hubungan_penjamin.text.toString().isEmpty()) {
            hubungan_penjamin_container.error = "Hubungan dengan Penjamin tidak boleh kosong"
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

            member.namaPenjamin = nama_penjamin.text.toString()
            member.hubunganPenjamin = hubungan_penjamin.text.toString()
            member.noHpPenjamin = no_hp_penjamin.text.toString()
            memberViewModel.setMember(member)

            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(6, true)

            val fragment = DokumenFragment()
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
         * @return A new instance of fragment DataPenjaminFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataPenjaminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}