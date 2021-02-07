package com.android.id.peers.pinjaman.pencairan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.id.peers.MainActivity
import com.android.id.peers.R
import com.android.id.peers.util.callback.PostPictureCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_POST_PICTURE
import com.android.id.peers.util.connection.ApiConnections.Companion.REQUEST_TYPE_PUT_STATUS_PENCAIRAN
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import kotlinx.android.synthetic.main.activity_pencairan_foto.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PencairanFotoActivity : AppCompatActivity() {
    private lateinit var pictureUri: Uri
    private lateinit var pictureName: String
    private lateinit var currentPhotoPath: String
    var idMember = 0
    var idPinjaman = 0
    var noKtp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pencairan_foto)

        idMember = intent.getIntExtra("id_member", 0)
        idPinjaman = intent.getIntExtra("id_pinjaman", 0)
        noKtp = intent.getStringExtra("no_ktp")!!

        foto.setOnClickListener {
            takePicture()
        }
        ulang.setOnClickListener {
            takePicture()
        }
        simpan.setOnClickListener {
            val preferences = getSharedPreferences("login_data", Context.MODE_PRIVATE)
//            val imgString = Base64.encodeToString(imageData, Base64.DEFAULT)
//            val imgData = Base64.decode(imgString, Base64.DEFAULT)

//            authenticate(preferences, this,
//                ApiConnections.REQUEST_TYPE_POST_MEMBER_PICTURE, object : PostPictureCallback {
//                override fun onPictureUploaded(fileName: String) {
////                    member.dokumenSlipGaji = fileName
////                    done++
//                }
//
//            }, imageData = imgData, fileName = pictureName)
            authenticate(preferences, this, REQUEST_TYPE_POST_PICTURE, null, imageData = imageData, fileName = pictureName, memberId = idMember)
            authenticate(preferences, this, REQUEST_TYPE_PUT_STATUS_PENCAIRAN, null, memberId = idMember, loanId = idPinjaman)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun takePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    100)
                Log.d("Dokumen Fragment", "Not Permitted")
            } else {
                Log.d("Dokumen Fragment", "Permitted")
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.d("PencairanFoto", "Permission has been denied by user")
            } else {
                dispatchTakePictureIntent()
                Log.d("PencairanFoto", "Permission has been granted by user")
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                pictureName = "IMG_DISBURSE_${noKtp}_$timeStamp.jpg"
                Log.d("PencairanFoto", "NAMA GAMBAR : $pictureName")

                val photoFile: File? = try {
                    createImageFile(pictureName)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.android.id.peers.file_provider",
                        it
                    )
                    pictureUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 100)
                }

            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(/*noId: String, documentType: String, toolType: String*/pictureName: String): File {
        // Create an image file name

        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("PencairanFoto", "onActivityResult ${data.toString()}")
        if (resultCode == Activity.RESULT_OK/* && requestCode == REQUEST_IMAGE_CAPTURE*/) {
//            val uri = data?.data
            var size: Long = 0

//            Log.d("PencairanFoto", "UPLOAD onActivityResult ${data.toString()}")
//                Log.d("PencairanFoto", "FILENAME onActivityResult ${File(data?.data!!.path!!).name}")

//            if (data?.data != null) {
//
//                data.data?.let { returnUri ->
//                    contentResolver.getType(returnUri)
//                    contentResolver.query(returnUri, null, null, null, null)
//                }?.use { cursor ->
//                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
//                    cursor.moveToFirst()
////                        pictureName = cursor.getString(nameIndex)
//                    size = cursor.getLong(sizeIndex)
//                }
//
//                uri = data.data!!
//                val photo = data.extras!!.get("data") as Bitmap
//                foto_pencairan.setImageBitmap(photo)
//            }

            var uri: Uri = pictureUri
//            Log.d("LoanDisburse", "onActivityResult selectedItemLoanId $selectedItemLoanId")
//            if (uri != null) {
//                imageView.setImageURI(uri)
//                Log.d("LoanDisburse", "onActivityResult $uri")
            var bitmap : Bitmap
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    uri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }

            foto_pencairan.setImageBitmap(bitmap)
            uri = resizeImage(bitmap, uri)
            createImageData(uri)
            foto_container.visibility = View.GONE
            foto.visibility = View.GONE
            button_container.visibility = View.VISIBLE
        }
    }

    private fun resizeImage(_bitmap: Bitmap, _uri: Uri) : Uri {
        var afd: AssetFileDescriptor? = contentResolver.openAssetFileDescriptor(_uri, "r")
        var fileSize: Long = afd!!.length
        afd.close()
        Log.d("PencairanFoto", "SIZE ORIGINAL: $fileSize")

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

        val imageOut = contentResolver.openOutputStream(_uri)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
            Log.d("PencairanFoto", "SIZE NEW BITMAP HERE : ${bitmap.width}")
        } finally {
            imageOut!!.close()
        }

        afd = contentResolver.openAssetFileDescriptor(_uri, "r")
        fileSize = afd!!.length
        afd.close()
        Log.d("PencairanFoto", "SIZE RESIZED : $fileSize")
        return _uri
    }
}