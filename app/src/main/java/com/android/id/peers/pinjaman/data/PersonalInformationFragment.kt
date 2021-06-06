package com.android.id.peers.pinjaman.data

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.members.models.MemberNikStatus
import com.android.id.peers.util.callback.ApiCallback
import com.android.id.peers.util.callback.MemberCallback
import com.android.id.peers.util.communication.MemberViewModel
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_GET_CHECK_MEMBER_BY_NIK
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.response.ApiResponse
import com.shuhart.stepview.StepView
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.button_bottom.*
import kotlinx.android.synthetic.main.fragment_occupation.*
import kotlinx.android.synthetic.main.fragment_personal_information.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PersonalInformationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PersonalInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalInformationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var fromFragment: Boolean = false
//    private var listener: OnFragmentInteractionListener? = null

    var connected: Boolean = true

    lateinit var preferences: SharedPreferences
    var rembugIds: ArrayList<Int> = ArrayList()
    var rembugData: ArrayList<String> = ArrayList()
    var kelompokData: ArrayList<String> = ArrayList()
    lateinit var kelompokAdapter: ArrayAdapter<String>
    lateinit var rembugAdapter: ArrayAdapter<String>

    override fun onDetach() {
        super.onDetach()
        Log.d("PersonalInfo", "DETACH FRAGMENT")
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        Log.d("PersonalInfo", "DESTROY FRAGMENT")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            fromFragment = it.getBoolean("from_address")
        }
        memberViewModel = ViewModelProvider(activity!!).get(MemberViewModel::class.java)

        val connectionStateMonitor = ConnectionStateMonitor(activity!!)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        preferences = context!!.getSharedPreferences("login_data", Context.MODE_PRIVATE)

        return inflater!!.inflate(R.layout.fragment_personal_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fromFragment) {
            identity_no.isEnabled = false
            periksa_container.visibility = View.GONE
            form_container.visibility = View.VISIBLE
        }

        checkConfig()

        rembugAndKelompok(view)

        periksa.setOnClickListener {
            /**
             * Call API to check whether the member is new or registered based on the [identity_no] field
             * Check whether [identity_no] is filled or not
             */
            if (identity_no.text.toString().isEmpty()) {
                identity_no_container.error = "No Identitas tidak boleh kosong"
            } else {
                val nik = identity_no.text.toString()

                if(nik.length < 16){
                    identity_no_container.error = "No Identitas tidak boleh kurang dari 16 digit"
                }else{
                    identity_no_container.error = null
                    progress_bar_holder.visibility = View.VISIBLE
                    authenticate(preferences, context!!, REQUEST_TYPE_GET_CHECK_MEMBER_BY_NIK,
                        object: MemberCallback {
                            override fun onSuccess(result: MemberNikStatus) {

                                progress_bar_holder.visibility = View.GONE
                                when (result.status) {
                                    /**
                                     * Continue to show the empty form
                                     */
                                    200 -> {
                                        periksa_container.visibility = View.GONE
                                        periksa.visibility = View.GONE
                                        form_container.visibility = View.VISIBLE
                                        identity_no.isEnabled = false
                                        val memberPreferences = context!!.getSharedPreferences("member_mode", Context.MODE_PRIVATE)
                                        memberPreferences.edit().putBoolean("existing_member", false).apply()
                                    }
                                    /**
                                     * Continue to show the pre-filled form
                                     */
                                    203 -> {
                                        memberViewModel.setMember(result.member!!)
                                        presetText(result.member!!)
                                        Log.d("PersonalInfo", result.member!!.noHp)
                                        periksa_container.visibility = View.GONE
                                        periksa.visibility = View.GONE
                                        form_container.visibility = View.VISIBLE
                                        identity_no.isEnabled = false

                                        val builder = AlertDialog.Builder(context!!)

                                        with(builder)
                                        {
                                            setTitle("Pemberitahuan")
                                            setMessage(result.message)
                                            setNeutralButton("OK", neutralButtonClick)
    //                                        setNegativeButton("No", negativeButtonClick)
                                            show()
                                        }

                                        val memberPreferences = context!!.getSharedPreferences("member_mode", Context.MODE_PRIVATE)
                                        memberPreferences.edit().putBoolean("existing_member", true).apply()
                                    }
                                    /**
                                     * There is an existing loan, the progress is halted
                                     * Showing []
                                     */
                                    else -> {
                                        val builder = AlertDialog.Builder(context!!)

                                        with(builder)
                                        {
                                            setTitle("Pemberitahuan")
                                            setMessage(result.message)
                                            setNeutralButton("OK", neutralButtonClick)
    //                                        setNegativeButton("No", negativeButtonClick)
                                            show()
                                        }
                                    }
                                }
                            }

                    }, nik = nik)
                }

            }
        }

        birth_date.keyListener = null
        birth_date.setOnClickListener { showDialog(view) }

        /* MaterialSpinner Adapters */
//        val identityAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.identity))
//        identityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        identity_type.adapter = identityAdapter
        val sexAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.sex))
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sex.adapter = sexAdapter
        val maritalStatusAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.marital_status))
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        marital_status.adapter = maritalStatusAdapter
        val lastEducationAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.last_education))
        lastEducationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        last_education.adapter = lastEducationAdapter

//        identity_type.setOnTouchListener { _, _ -> true }

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
            member ->
//            identity_type.selection = member.jenisIdentitas
            presetText(member)
        })
        back.setOnClickListener { onBackButtonClicked() }
        next.setOnClickListener { onNextButtonClicked(view) }
    }

    val neutralButtonClick = { dialog: DialogInterface, which: Int ->

    }

    /**
     * Fill the value of input forms pre-determined by Member gotten from the server
     */
    private fun presetText(member: Member) {
        identity_no.setText(member.noIdentitas)
        full_name.setText(member.namaLengkap)
        email.setText(member.email)
        handphone_no.setText(member.noHp)
        birth_date.setText(member.tanggalLahir)
        birth_place.setText(member.tempatLahir)
        usia.setText(member.usia)
        sex.selection = member.jenisKelamin
        mother_name.setText(member.namaGadisIbuKandung)
        marital_status.selection = member.statusPerkawinan
        last_education.selection = member.pendidikanTerakhir

        rembug.selection = rembugAdapter.getPosition(member.selectedRembug)
        kelompok.selection = kelompokAdapter.getPosition(member.selectedKelompok)
    }

    fun rembugAndKelompok(view: View){
        authenticate(preferences, context!!, ApiConnections.REQUEST_TYPE_GET_REMBUG,
            object : ApiCallback {
                override fun onSuccess(result: Any) {
                    var data = result as ApiResponse
                    val d = data!!.data as ArrayList<Any>
                    var newData = JSONArray(d)
                    rembugData.clear()
                    rembugIds.clear()
                    for (idx in 0 until newData.length()) {
                        val rembugObject = newData.getJSONObject(idx)
                        val rembugObjectText = rembugObject.getString("nama_ketua")+" - "+rembugObject.getString("nama_rembug")
                        rembugData.add(rembugObjectText)
                        rembugIds.add(rembugObject.getInt("id"))
                    }

                }
            })

        rembugAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, rembugData)
        rembugAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rembug.adapter = rembugAdapter

        rembug.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                getKelompok(rembugIds.get(position))
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
//                TODO("Not yet implemented")
            }

        }

        kelompokAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, kelompokData)
        kelompokAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kelompok.adapter = kelompokAdapter
    }

    fun getKelompok(idRembug : Int){
        authenticate(preferences, context!!, ApiConnections.REQUEST_TYPE_GET_KELOMPOK,
            object : ApiCallback {
                override fun onSuccess(result: Any) {
                    var data = result as ApiResponse
                    val d = data!!.data as ArrayList<Any>
                    var newData = JSONArray(d)
                    kelompokData.clear()
                    for (idx in 0 until newData.length()) {
                        val kelompokObject = newData.getJSONObject(idx)
                        val kelompokObjectText = kelompokObject.getString("nama_ketua")+" - "+kelompokObject.getString("nama_kelompok")
                        kelompokData.add(kelompokObjectText)
                    }

                }
            },rembugId = idRembug)

        kelompok.visibility = View.VISIBLE
    }

    /**
     * Check config Preferences as the rule to show the form inputs
     */
    private fun checkConfig() {
        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

//        if (configPreferences.getInt("jenis_identitas", 1) == 0) identity_type.visibility = View.GONE
        if (configPreferences.getInt("no_identitas", 1) == 0) {
            identity_no.visibility = View.GONE
            identity_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("email", 1) == 0) {
            email.visibility = View.GONE
            email_container.visibility = View.GONE
        }
        if (configPreferences.getInt("nama_lengkap", 1) == 0) {
            full_name.visibility = View.GONE
            full_name_container.visibility = View.GONE
        }
        if (configPreferences.getInt("no_hp", 1) == 0) {
            handphone_no.visibility = View.GONE
            handphone_no_container.visibility = View.GONE
        }
        if (configPreferences.getInt("tanggal_lahir", 1) == 0) {
            birth_date.visibility = View.GONE
            birth_place_container.visibility = View.GONE
        }
        if (configPreferences.getInt("tempat_lahir", 1) == 0) {
            birth_place.visibility = View.GONE
            birth_place_container.visibility = View.GONE
        }
        if (configPreferences.getInt("usia", 1) == 0) {
            usia.visibility = View.GONE
            usia_container.visibility = View.GONE
        }
        if (configPreferences.getInt("jenis_kelamin", 1) == 0) sex.visibility = View.GONE
        if (configPreferences.getInt("nama_gadis_ibu", 1) == 0) {
            mother_name.visibility = View.GONE
            mother_name_container.visibility = View.GONE
        }
        if (configPreferences.getInt("status_perkawinan", 1) == 0) marital_status.visibility = View.GONE
        if (configPreferences.getInt("pendidikan_terakhir", 1) == 0) last_education.visibility = View.GONE

        if (configPreferences.getInt("rembug", 1) == 0) rembug.visibility = View.GONE
        if (configPreferences.getInt("kelompok", 1) == 0) kelompok.visibility = View.GONE
    }

    private  fun onBackButtonClicked() {
        identity_no.isEnabled = true
        periksa_container.visibility = View.VISIBLE
        periksa.visibility = View.VISIBLE
        form_container.visibility = View.GONE
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

//        if(configPreferences.getInt("jenis_identitas", 1) == 1 && identity_type.selectedItemId < 0) {
//            identity_type.error = "Jenis Identitas tidak boleh kosong"
//            allTrue = false
//        }
        if(configPreferences.getInt("no_identitas", 1) == 1 && identity_no.text.toString().isEmpty()) {
            identity_no_container.error = "No Identitas tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("nama_lengkap", 1) == 1 && full_name.text.toString().isEmpty()) {
            full_name_container.error = "Nama Lengkap tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("email", 1) == 1 && email.text.toString().isEmpty()) {
            email_container.error = "Alamat Email tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("no_hp", 1) == 1 && handphone_no.text.toString().isEmpty()) {
            handphone_no_container.error = "No Handphone tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("tanggal_lahir", 1) == 1 && birth_date.text.toString().isEmpty()) {
            birth_date_container.error = "Tanggal Lahir tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("tempat_lahir", 1) == 1 && birth_place.text.toString().isEmpty()) {
            birth_place_container.error = "Tempat Lahir tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("usia", 1) == 1 && usia.text.toString().isEmpty()) {
            usia_container.error = "Usia tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("jenis_kelamin", 1) == 1 && sex.selectedItemId < 0) {
            sex.error = "Jenis Kelamin tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("nama_gadis_ibu", 1) == 1 && mother_name.text.toString().isEmpty()) {
            mother_name_container.error = "Nama Gadis Ibu Kandung tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("status_perkawinan", 1) == 1 && marital_status.selectedItemId < 0) {
            marital_status.error = "Status Pernikahan tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("pendidikan_terakhir", 1) == 1 && last_education.selectedItemId < 0) {
            last_education.error = "Pendidikan Terakhir tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("rembug", 1) == 1 && rembug.selectedItemId < 0) {
            rembug.error = "Rembug tidak boleh kosong"
            allTrue = false
        }
        if(configPreferences.getInt("kelompok", 1) == 1 && kelompok.selectedItemId < 0) {
            kelompok.error = "Kelompok tidak boleh kosong"
            allTrue = false
        }
        if(allTrue) {

            var member = memberViewModel.member.value
            if(member == null) {
                member = Member()
            }
            setMember(member)

            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(1, true)

            val fragment = AddressFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.member_acquisition_fragment_container, fragment)?.addToBackStack(null)?.commit()
            transaction?.replace(R.id.member_acquisition_fragment_container, fragment)?.commit()

//            val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//            memberStatusView.statusView.run {
//                currentCount += 1
//            }

//            val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//            connected = activeNetwork?.isConnectedOrConnecting == true
//
//            if (connected) {
//                val mainView = activity!!.window.decorView
//                progress_bar_holder.visibility = View.VISIBLE
////                authenticate(activity!!.getSharedPreferences("login_data", Context.MODE_PRIVATE),
////                    activity!!, ApiConnections.REQUEST_TYPE_GET_MEMBER_BY_PHONE, object:
////                        MemberCallback {
////                        override fun onSuccess(result: Member) {
////                            progress_bar_holder.visibility = View.GONE
////                            if (result.id == 0) {
////                                val stepView = activity!!.findViewById<StepView>(R.id.step_view)
////                                stepView.go(1, true)
////
////                                var member = memberViewModel.member.value
////                                if(member == null) {
////                                    member = Member()
////                                }
////                                setMember(member!!)
////
////                                val fragment = AddressFragment()
////                                val transaction = activity?.supportFragmentManager?.beginTransaction()
////                                transaction?.replace(R.id.member_acquisition_fragment_container, fragment)?.addToBackStack(null)?.commit()
////                            } else {
////                                PeersSnackbar.popUpSnack(mainView, "Phone number has already been registered!")
////                            }
////                        }
////                    }
////                    , memberPhone = handphone_no.text.toString())
//            } else {
//                val stepView = activity!!.findViewById<StepView>(R.id.step_view)
//                stepView.go(1, true)
//
//                var member = memberViewModel.member.value
//                if(member == null) {
//                    member = Member()
//                }
//                setMember(member!!)
//
//                val fragment = AddressFragment()
//                val transaction = activity?.supportFragmentManager?.beginTransaction()
//                transaction?.replace(R.id.member_acquisition_fragment_container, fragment)?.addToBackStack(null)?.commit()
//            }

            /* else {
                val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
                    OfflineViewModel::class.java
                )
                offlineViewModel.insertMember(member)
            }*/
        }
    }

    private fun setMember(member: Member) {
//        member.jenisIdentitas = identity_type.selection
        member.noIdentitas = identity_no.text.toString()
        member.namaLengkap = full_name.text.toString()
        member.email = email.text.toString()
        member.noHp = handphone_no.text.toString()
        member.tanggalLahir = birth_date.text.toString()
        member.tempatLahir = birth_place.text.toString()
        member.usia = usia.text.toString()
        member.jenisKelamin = sex.selection
        member.namaGadisIbuKandung = mother_name.text.toString()
        member.statusPerkawinan = marital_status.selection
        member.pendidikanTerakhir = last_education.selection
        member.rembug = rembug.selection
        member.kelompok = kelompok.selection
        member.selectedRembug = rembug.selectedItem.toString()
        member.selectedKelompok = kelompok.selectedItem.toString()

        memberViewModel.setMember(member)
    }

    fun showDialog(_view: View) {
        val c = Calendar.getInstance()
        val _year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(_view.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            val placeholder = sdf.format(c.time)
            birth_date.setText(placeholder)
        }, _year, month, day)

        datePickerDialog.show()
    }

    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

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
         * @return A new instance of fragment PersonalInformationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
