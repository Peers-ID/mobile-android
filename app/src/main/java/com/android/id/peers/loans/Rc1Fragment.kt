package com.android.id.peers.loans

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
import kotlinx.android.synthetic.main.fragment_rc1.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Use the [Rc1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Rc1Fragment : Fragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rc1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> { 
            member ->
            identity.text = member.memberJenisIdentitasString()
            identity_no.text = member.noIdentitas
            full_name.text = member.namaLengkap
            handphone_no.text = member.noHp
            birth_date.text = member.tanggalLahir
            birth_place.text = member.tempatLahir
            Log.d("Rc1Fragment", "${member.jenisKelamin}")
            sex.text = member.jenisKelaminString()
            mother_name.text = member.namaGadisIbuKandung
            marital_status.text = member.statusPerkawinanString()
            last_education.text = member.pendidikanTerakhirString()
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Rc1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Rc1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
