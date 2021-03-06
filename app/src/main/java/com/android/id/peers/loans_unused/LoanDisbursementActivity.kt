package com.android.id.peers.loans_unused

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.android.id.peers.BuildConfig
import com.android.id.peers.R
import com.android.id.peers.loans_unused.adapters.LoansAdapter
import com.android.id.peers.loans_unused.models.Loan
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.ApiConnections.Companion.authenticate
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.workers.LoanPictureWorker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_loan_disbursement.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LoanDisbursementActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val MY_CAMERA_REQUEST_CODE = 100
    lateinit var pictureUri: Uri
    lateinit var pictureName: String

//    val loans: ArrayList<LoanItem> = ArrayList()
    var loans: ArrayList<Loan> = ArrayList()
//    var loan: ArrayList<Loan> = ArrayList()
    lateinit var loan: List<Loan>
    lateinit var selectedLoan: Loan
    val activity = this
    val loansAdapter = LoansAdapter(loans, activity, 0) { loan : Loan -> loanItemClicked(loan)}

    var selectedItemLoanId = 0

    var connected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_disbursement)

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

        title = "Loan Disbursement"

        pembayaran_cicilan.adapter = loansAdapter

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        connected = activeNetwork?.isConnectedOrConnecting == true

        if (connected) {
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmerAnimation()
            authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
                    LoanDisbursement {
                    override fun onSuccess(result: List<Loan>) {
                        loan = ArrayList<Loan>(result)
                        for (l in loan) {
                            val loanDisburseItem = Loan(otherFees = ArrayList())
//                        loanDisburseItem.loanNo = ""
                            loanDisburseItem.memberId = l.memberId
                            loanDisburseItem.memberName = l.memberName
                            loanDisburseItem.totalDisbursed = l.totalDisbursed
                            loans.add(loanDisburseItem)
                        }

                        val loanPreferences = getSharedPreferences("loans", Context.MODE_PRIVATE)
                        Loan.saveLoans(loanPreferences, result, "loan_disburse")

//                    loan_disbursement.adapter!!.notifyDataSetChanged()
                        loansAdapter.notifyDataSetChanged()
                        shimmer_view_container.stopShimmerAnimation()
                        shimmer_view_container.visibility = View.GONE
                    }
                }
                , listType = 3)
        } else {
            val loanPreferences = getSharedPreferences("loans", Context.MODE_PRIVATE)
            val loanJson = loanPreferences.getString("loan_disburse", null)!!
            loans = Gson().fromJson(loanJson)
            loansAdapter.notifyDataSetChanged()
        }

        pembayaran_cicilan.setHasFixedSize(true);
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        pembayaran_cicilan.layoutManager = llm

//        loans = ArrayList()
//        loan_disbursement.adapter = LoansAdapter(loans, activity, 0) { loan : LoanItem -> loanItemClicked(loan)}

        pembayaran_cicilan.addItemDecoration(
            DividerItemDecoration(this,
            DividerItemDecoration.HORIZONTAL)
        )
        pembayaran_cicilan.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))

        pembayaran_cicilan.setOnClickListener {
            /** A safe way to get an instance of the Camera object. */


        }
    }

//    private fun loanItemClicked(loan : LoanItem) {
    private fun loanItemClicked(loan : Loan) {
        Toast.makeText(this, "Clicked: ${loan.memberName}", Toast.LENGTH_LONG).show()
//        val intent = Intent(this, RepaymentCollectionDetailActivity::class.java)
//        startActivity(intent)

        selectedItemLoanId = loan.memberId
        selectedLoan = loan

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    MY_CAMERA_REQUEST_CODE)
                Log.d("LoanDisburse", "Not Permitted")
            } else {
                Log.d("LoanDisburse", "Permitted")
                dispatchTakePictureIntent(loan)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.d("LoanDisburse", "Permission has been denied by user")
        } else {
            dispatchTakePictureIntent(selectedLoan)
            Log.d("LoanDisburse", "Permission has been granted by user")
        }
    }

    private fun dispatchTakePictureIntent(loan: Loan) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile(loan)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${BuildConfig.APPLICATION_ID}.file_provider",
                        it
                    )
                    pictureUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }

            }
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(loan: Loan): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        pictureName = "IMG_${loan.memberName}${loan.memberId}${loan.formulaId}_$timeStamp"
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
        Log.d("LoanDisburse", "onActivityResult ${data.toString()}")
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
//            val uri = data?.data
            val uri = pictureUri
            Log.d("LoanDisburse", "onActivityResult selectedItemLoanId $selectedItemLoanId")
//            if (uri != null) {
//                imageView.setImageURI(uri)
//                Log.d("LoanDisburse", "onActivityResult $uri")
            createImageData(uri)

            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            connected = activeNetwork?.isConnectedOrConnecting == true

            if (connected) {
                authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                    this, ApiConnections.REQUEST_TYPE_POST_PICTURE, imageData, memberId = selectedItemLoanId, loanId = selectedLoan.id, fileName = pictureName)
                val fDelete = File(uri.path!!)
                if (fDelete.delete()) {
                    Log.d("LoanDisburse","File is deleted" )
                } else {
                    Log.d("LoanDisburse","File is not deleted" )
                }

                shimmer_view_container.visibility = View.VISIBLE
                shimmer_view_container.startShimmerAnimation()
                authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                    this, ApiConnections.REQUEST_TYPE_GET_LOAN, object :
                    LoanDisbursement {
                        override fun onSuccess(result: List<Loan>) {
                            loans.clear()
                            loan = ArrayList<Loan>(result)
                            loans.addAll(loan)
                            Log.d("LoanDisburse", "Loan Size : ${loans.size}")
//                                loan_disbursement.adapter!!.notifyDataSetChanged()
                            loansAdapter.notifyDataSetChanged()
                            shimmer_view_container.stopShimmerAnimation()
                            shimmer_view_container.visibility = View.GONE
                            PeersSnackbar.popUpSnack(activity.window.decorView, "Loan disbursed successfully!")
                        }
                    }
                    , listType = 3)
            } else {
//                val loanPicture = LoanPicture(0, selectedItemLoanId, pictureName)
//                val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
//                    OfflineViewModel::class.java)
//                offlineViewModel.insertLoanPictures(loanPicture)

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val data1 = Data.Builder()
                data1.put("image_data", imageData)
                data1.put("member_id", selectedItemLoanId)
                data1.put("loan_id", selectedLoan.id)
                data1.put("file_name", pictureName)
                val loanPictureWorker = OneTimeWorkRequestBuilder<LoanPictureWorker>()
                    .setConstraints(constraints)
                    .setInputData(data1.build())
                    .build() // or PeriodicWorkRequest

                loans.remove(selectedLoan)

                WorkManager.getInstance(activity).enqueue(loanPictureWorker)

                PeersSnackbar.popUpSnack(activity.window.decorView, "There was no internet access! Data is saved locally")
            }
//            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inline fun <reified T> Gson.fromJson(json: String): T = fromJson<T>(json, object: TypeToken<T>() {}.type)
}
