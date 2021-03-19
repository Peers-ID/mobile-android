package com.android.id.peers.pinjaman.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.id.peers.R
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.communication.MemberViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shuhart.stepview.StepView
import kotlinx.android.synthetic.main.button_bottom.*
import kotlinx.android.synthetic.main.fragment_dokumen.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var memberViewModel = MemberViewModel()

private const val REQUEST_CODE_CAMERA_KTP = 101
private const val REQUEST_CODE_CAMERA_SIM = 102
private const val REQUEST_CODE_CAMERA_KK = 103
private const val REQUEST_CODE_CAMERA_KETERANGAN_KERJA = 104
private const val REQUEST_CODE_CAMERA_BPKB = 105
private const val REQUEST_CODE_CAMERA_AKTA_NIKAH = 106
private const val REQUEST_CODE_CAMERA_SLIP_GAJI = 107
private const val REQUEST_CODE_CAMERA_LAINNYA = 108

private const val REQUEST_CODE_UPLOAD_KTP = 201
private const val REQUEST_CODE_UPLOAD_SIM = 202
private const val REQUEST_CODE_UPLOAD_KK = 203
private const val REQUEST_CODE_UPLOAD_KETERANGAN_KERJA = 204
private const val REQUEST_CODE_UPLOAD_BPKB = 205
private const val REQUEST_CODE_UPLOAD_AKTA_NIKAH = 206
private const val REQUEST_CODE_UPLOAD_SLIP_GAJI = 207
private const val REQUEST_CODE_UPLOAD_LAINNYA = 208

private const val MY_CAMERA_REQUEST_CODE = 100

private lateinit var pictureUri: Uri
private lateinit var pictureName: String

/**
 * A simple [Fragment] subclass.
 * Use the [DokumenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DokumenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var memberId: String = ""

    private lateinit var anggotaContext: Context
    
    private var documentType = ""
    private var toolType = ""

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
        return inflater.inflate(R.layout.fragment_dokumen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DokumenFragment", " NO HP : ${memberViewModel.member.value!!.noHp}")
        next.setOnClickListener { onNextButtonClicked(view) }
        back.setOnClickListener { onBackButtonClicked(view) }

        anggotaContext = context!!

        /* Member View Model */
        memberViewModel.member.observe(viewLifecycleOwner, Observer<Member> {
                member ->

//            Log.d("DokumenFragment", "WOI! : ${member.noIdentitas}")
            memberId = member.noIdentitas
            ktp_text.text = member.dokumenKtp
            sim_text.text = member.dokumenSim
            kk_text.text = member.dokumenKk
            ket_kerja_text.text = member.dokumenKetKerja
            akta_nikah_text.text = member.dokumenAktaNikah
            bpkb_text.text = member.dokumenBpkb
            slip_gaji_text.text = member.dokumenSlipGaji
            lainnya_text.text = member.dokumenLainnya
        })

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)

        if (configPreferences.getInt("dokumen_ktp", 1) == 0) {
            ktp_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_sim", 1) == 0) {
            sim_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_kk", 1) == 0) {
            kk_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_keterangan_kerja", 1) == 0) {
            ket_kerja_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_bpkb", 1) == 0) {
            bpkb_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_akta_nikah", 1) == 0) {
            akta_nikah_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_slip_gaji", 1) == 0) {
            slip_gaji_container.visibility = View.GONE
        }
        if (configPreferences.getInt("dokumen_lainnya", 1) == 0) {
            lainnya_container.visibility = View.GONE
        }
        ktp_camera.setOnClickListener {
            documentType = "KTP"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_KTP, memberId)
        }
        sim_camera.setOnClickListener {
            documentType = "SIM"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_SIM, memberId)
        }
        kk_camera.setOnClickListener {
            documentType = "KK"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_KK, memberId)
        }
        ket_kerja_camera.setOnClickListener {
            documentType = "KET_KERJA"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_KETERANGAN_KERJA, memberId)
        }
        bpkb_camera.setOnClickListener {
            documentType = "BPKB"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_BPKB, memberId)
        }
        akta_nikah_camera.setOnClickListener {
            documentType = "AKTA_NIKAH"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_AKTA_NIKAH, memberId)
        }
        slip_gaji_camera.setOnClickListener {
            documentType = "SLIP_GAJI"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_SLIP_GAJI, memberId)
        }
        lainnya_camera.setOnClickListener {
            documentType = "DLL"
            toolType = "CAMERA"
            cameraButtonClicked(REQUEST_CODE_CAMERA_LAINNYA, memberId)
        }
        ktp_upload.setOnClickListener {
            documentType = "KTP"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_KTP, memberId)
        }
        sim_upload.setOnClickListener {
            documentType = "SIM"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_SIM, memberId)
        }
        kk_upload.setOnClickListener {
            documentType = "KK"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_KK,  memberId)
        }
        ket_kerja_upload.setOnClickListener {
            documentType = "KET_KERJA"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_KETERANGAN_KERJA, memberId)
        }
        bpkb_upload.setOnClickListener {
            documentType = "BPKB"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_BPKB, memberId)
        }
        akta_nikah_upload.setOnClickListener {
            documentType = "AKTA_NIKAH"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_AKTA_NIKAH, memberId)
        }
        slip_gaji_upload.setOnClickListener {
            documentType = "SLIP_GAJI"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_SLIP_GAJI, memberId)
        }
        lainnya_upload.setOnClickListener {
            documentType = "DLL"
            toolType = "UPLOAD"
            uploadButtonClicked(REQUEST_CODE_UPLOAD_LAINNYA, memberId)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.d("DokumenFragment", "Permission has been denied by user")
        } else {
//            if (requestCode / 100 == 1) {
            dispatchTakePictureIntent(requestCode, memberId)
//            } else {
//                dispatchMediaFileBrower(requestCode, memberId)
//            }
            Log.d("DokumenFragment", "Permission has been granted by user")
        }
    }

    private fun cameraButtonClicked(requestCode: Int, noId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(anggotaContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    requestCode)
                Log.d("Dokumen Fragment", "Not Permitted")
            } else {
                Log.d("Dokumen Fragment", "Permitted")
                dispatchTakePictureIntent(requestCode, noId)
            }
        }
    }

    private fun uploadButtonClicked(requestCode: Int, noId: String) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            if (checkSelfPermission(anggotaContext, Manifest.permission.ACCESS_MEDIA_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION),
//                    requestCode)
//                Log.d("Dokumen Fragment", "Not Permitted")
//            } else {
//                Log.d("Dokumen Fragment", "Permitted")
//                dispatchMediaFileBrower(requestCode, noId)
//            }
//        }
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        pictureName = "IMG_${documentType}_${toolType}_${noId}_$timeStamp.jpg"
        dispatchMediaFileBrower(requestCode, noId)
    }

    private fun dispatchMediaFileBrower(requestCode: Int, noId: String) {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), requestCode)
    }

    private fun dispatchTakePictureIntent(requestCode: Int, noId: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(anggotaContext.packageManager)?.also {
                // Create the File where the photo should go

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                pictureName = "IMG_${documentType}_${toolType}_${noId}_$timeStamp.jpg"
                Log.d("DokumenFragment", "NAMA GAMBAR : $pictureName")

                val photoFile: File? = try {
                    createImageFile(pictureName)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        anggotaContext,
                        "com.android.id.peers.file_provider",
                        it
                    )
                    pictureUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestCode)
                }
            }
        }
    }


    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(/*noId: String, documentType: String, toolType: String*/pictureName: String): File {
        // Create an image file name

        val storageDir: File? = anggotaContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            pictureName, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private var imageData: ByteArray? = null

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = anggotaContext.contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("DokumenFragment", "onActivityResult ${data.toString()}")

        if (resultCode == Activity.RESULT_OK/* && requestCode == REQUEST_IMAGE_CAPTURE*/) {
//            val uri = data?.data
            var size: Long = 0

            lateinit var uri: Uri
            if (requestCode / 100 == 2) {
                Log.d("DokumenFragment", "UPLOAD onActivityResult ${data.toString()}")
//                Log.d("DokumenFragment", "FILENAME onActivityResult ${File(data?.data!!.path!!).name}")

                if (data?.data != null) {

                    data.data?.let { returnUri ->
                        anggotaContext.contentResolver.getType(returnUri)
                        anggotaContext.contentResolver.query(returnUri, null, null, null, null)
                    }?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        cursor.moveToFirst()
//                        pictureName = cursor.getString(nameIndex)
                        size = cursor.getLong(sizeIndex)
                    }

//                    val file = File(data.data!!.path!!)

                    uri = data.data!!
                }
            } else {
                uri = pictureUri
            }

            val bitmap : Bitmap
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(
                    this.context!!.contentResolver,
                    uri)
            } else {
                val source = ImageDecoder.createSource(context!!.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }

//            Log.d("LoanDisburse", "onActivityResult selectedItemLoanId $selectedItemLoanId")
//            if (uri != null) {
//                imageView.setImageURI(uri)
//                Log.d("LoanDisburse", "onActivityResult $uri")
            uri = resizeImage(bitmap, uri)
            createImageData(uri)

            var member = memberViewModel.member.value
            if(member == null) {
                member = Member()
            }

            when (documentType) {
                "KTP" -> {
                    Log.d("DokumenFragment", "KTP")
                    ktp_text.text = pictureName
                    member.dokumenKtp = pictureName

                    member.dokumenKtpByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    Log.d("DokumenFragment", "KTP TEXT : ${member.dokumenKtpByteArrayString}")

                }
                "SIM" -> {
                    member.dokumenSimByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    sim_text.text = pictureName
                    member.dokumenSim = pictureName
                }
                "KK" -> {
                    member.dokumenKkByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    kk_text.text = pictureName
                    member.dokumenKk = pictureName
                }
                "KET_KERJA" -> {
                    member.dokumenKetKerjaByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    ket_kerja_text.text = pictureName
                    member.dokumenKetKerja = pictureName
                }
                "BPKB" -> {
                    member.dokumenBpkbByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    bpkb_text.text = pictureName
                    member.dokumenBpkb = pictureName
                }
                "AKTA_NIKAH" -> {
                    member.dokumenAktaNikahByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    akta_nikah_text.text = pictureName
                    member.dokumenAktaNikah = pictureName
                }
                "SLIP_GAJI" -> {
                    member.dokumenSlipGajiByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    slip_gaji_text.text = pictureName
                    member.dokumenSlipGaji = pictureName
                }
                else -> {
                    member.dokumenLainnyaByteArrayString = Base64.encodeToString(imageData, Base64.DEFAULT)
                    lainnya_text.text = pictureName
                    member.dokumenLainnya = pictureName
                }
            }

            memberViewModel.setMember(member)
        }
    }

    private fun resizeImage(_bitmap: Bitmap, _uri: Uri) : Uri {
//        var afd: AssetFileDescriptor? = context!!.contentResolver.openAssetFileDescriptor(_uri, "r")
//        var fileSize: Long = afd!!.length
//        afd.close()
//        Log.d("PencairanFoto", "SIZE ORIGINAL: $fileSize")

        var bitmap = _bitmap
        Log.d("PencairanFoto", "SIZE BITMAP : ${bitmap.width}")
        if (bitmap.width > 100) {
            val divider = bitmap.width / 100
            val width = 100
            val height = bitmap.height / divider
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        }

        Log.d("PencairanFoto", "SIZE NEW BITMAP : ${bitmap.width}")

//        val bytes = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val storageDir: File? = anggotaContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempFile = File.createTempFile(
            pictureName, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bitmapData = bytes.toByteArray()

        //write the bytes in file

        //write the bytes in file
        val fos = FileOutputStream(tempFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

//        val imageOut = context!!.contentResolver.openOutputStream(_uri)
//        try {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
//            Log.d("PencairanFoto", "SIZE NEW BITMAP HERE : ${bitmap.width}")
//        } finally {
//            imageOut!!.close()
//        }

//        afd = context!!.contentResolver.openAssetFileDescriptor(_uri, "r")
//        fileSize = afd!!.length
//        afd.close()
//        Log.d("PencairanFoto", "SIZE RESIZED : $fileSize")
        return Uri.fromFile(tempFile)
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object: TypeToken<T>() {}.type)

    private fun onBackButtonClicked(view: View) {
//        val memberStatusView = activity!!.findViewById<StatusViewScroller>(R.id.status_view_member_acquisition)
//        memberStatusView.statusView.run {
//            currentCount -= 1
//        }
        val stepView = activity!!.findViewById<StepView>(R.id.step_view)
        stepView.go(5, true)

        var member = memberViewModel.member.value
        if(member == null) {
            member = Member()
        }

        setMember(member)

        val fragment = DataPenjaminFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()
//        activity!!.onBackPressed()
    }

    private fun onNextButtonClicked(view: View) {
        var allTrue = true

        Log.d("DokumenFragment", "KTP TEXT : ${ktp_text.text}")

        val configPreferences: SharedPreferences = activity!!.getSharedPreferences("member_config", Context.MODE_PRIVATE)
//        val preferences = activity!!.getSharedPreferences("login_data", Context.MODE_PRIVATE)
//        val member = memberViewModel.member.value!!
//
//        authenticate(preferences, context!!, REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
//            override fun onSuccess(result: String) {
//
//            }
//
//        }, imageData = member.dokumenKtpByteArrayString.toByteArray(), fileName = member.dokumenKtp)

        if(configPreferences.getInt("dokumen_ktp", 1) == 1 && ktp_text.text.toString().isEmpty()) {
            ktp_title.text = "Dokumen KTP tidak boleh kosong"
            ktp_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_sim", 1) == 1 && sim_text.text.toString().isEmpty()) {
            sim_title.text = "Dokumen SIM tidak boleh kosong"
            sim_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_kk", 1) == 1 && kk_text.text.toString().isEmpty()) {
            kk_title.text = "Dokumen KK tidak boleh kosong"
            kk_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_bpkb", 1) == 1 && bpkb_text.text.toString().isEmpty()) {
            bpkb_title.text = "Dokumen BPKB tidak boleh kosong"
            bpkb_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_keterangan_kerja", 1) == 1 && ket_kerja_text.text.toString().isEmpty()) {
            ket_kerja_title.text = "Dokumen Keterangan Kerja tidak boleh kosong"
            ket_kerja_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_slip_gaji", 1) == 1 && slip_gaji_text.text.toString().isEmpty()) {
            slip_gaji_title.text = "Dokumen Slip Gaji tidak boleh kosong"
            slip_gaji_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_akta_nikah", 1) == 1 && akta_nikah_text.text.toString().isEmpty()) {
            akta_nikah_title.text = "Dokumen Akta Nikah tidak boleh kosong"
            akta_nikah_title.setTextColor(Color.RED)
            allTrue = false
        }
        if(configPreferences.getInt("dokumen_lainnya", 1) == 1 && lainnya_text.text.toString().isEmpty()) {
            lainnya_title.text = "Dokumen Lainnya tidak boleh kosong"
            lainnya_title.setTextColor(Color.RED)
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

            val stepView = activity!!.findViewById<StepView>(R.id.step_view)
            stepView.go(7, true)

            val fragment = SurveyFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.member_acquisition_fragment_container, fragment).commit()

//            val intent = Intent(activity, MemberAcquisitionConfirmationActivity::class.java)
//            intent.putExtra("member", memberViewModel.member.value)
//            startActivity(intent)
        }
    }

    private fun setMember(member: Member) {
        member.dokumenKtp = ktp_text.text.toString()
        member.dokumenSim = sim_text.text.toString()
        member.dokumenKk = kk_text.text.toString()
        member.dokumenKetKerja = ket_kerja_text.text.toString()
        member.dokumenAktaNikah = akta_nikah_text.text.toString()
        member.dokumenBpkb = bpkb_text.text.toString()
        member.dokumenSlipGaji = slip_gaji_text.text.toString()
        member.dokumenLainnya = lainnya_text.text.toString()

        memberViewModel.setMember(member)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DokumenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DokumenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}