package com.android.id.peers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.android.id.peers.members.models.Member
import com.android.id.peers.pinjaman.pengajuan.Pinjaman
import com.android.id.peers.util.PeersSnackbar
import com.android.id.peers.util.connection.ConnectionStateMonitor
import com.android.id.peers.util.connection.NetworkConnectivity
import com.android.id.peers.util.database.OfflineViewModel
import com.android.id.peers.util.workers.MemberWorker
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TermsActivity : AppCompatActivity() {

    private var noHP = ""
    var member: Member? = null
    var pinjaman: Pinjaman? = null
    val context = this

    var connected: Boolean = true

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val connectionStateMonitor = ConnectionStateMonitor(application)
        connectionStateMonitor.observe(this, Observer { isConnected ->
            connected = isConnected
        })

//        if (intent.extras != null) {
        if (intent.getParcelableExtra<Member>("member") != null) {
            member = intent.getParcelableExtra("member")
        }
        if (intent.getParcelableExtra<Pinjaman>("pinjaman") != null) {
            pinjaman = intent.getParcelableExtra("pinjaman")
        }
//            if (intent.getStringExtra("member_handphone") != null) {
//                noHP = (intent.getStringExtra("member_handphone"))!!
//            }
//        }

        Log.d("TermsActivity", "NO HP : ${member!!.noHp}")

        var parsedText = ""

        GlobalScope.launch(Dispatchers.IO) {

            val reader = PdfReader("http://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf")
            val n = reader.numberOfPages

            for (i in 0 until n) {
                Log.d("TermsActivity", "I : $i")
                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"
            }
//            Log.d("TermsActivity", "Text : $parsedText")
            reader.close()
            terms_text_area.text = parsedText
        }

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels

//        Log.d("TermsActivity", String.format("%d", termsTextArea.layoutParams.height))

//        Log.d("TermsActivity", "GG" + String.format("%d", resources.getDimension(R.dimen.activity_vertical_margin).toInt()))
        terms_text_area.movementMethod = ScrollingMovementMethod()
        terms_text_area.requestLayout()

        lanjutkan.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                lanjutkan.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val temp = height - terms_text.height - agree.height - lanjutkan.height - 7 * resources.getDimension(R.dimen.activity_vertical_margin).toInt() - resources.getDimension(R.dimen.margin_between).toInt()
                terms_text_area.layoutParams.height = temp
                terms_text_area.requestLayout()
            }
        })

        lanjutkan.setOnClickListener {
            if(!agree.isChecked) {
                agree.supportButtonTintList = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
                agree.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }else {

                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                connected = activeNetwork?.isConnectedOrConnecting == true

                if (connected) {
                    val intent = Intent(this, VerificationActivity::class.java)
//                    if (member != null) {
                    intent.putExtra("member", member)
                    intent.putExtra("pinjaman", pinjaman)
//                    }
//                    intent.putExtra("hand_phone", noHP)
                    startActivity(intent)
                }/* else {*/
//                    val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
//                        OfflineViewModel::class.java)
//                    if (member != null) {
//                        offlineViewModel.insertMember(member!!)
//                    }
//                    val constraints = Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build()
//                    val data = Member.putMemberOnDataBuilder(member!!)
//                    val memberWorker = OneTimeWorkRequestBuilder<MemberWorker>()
//                        .setConstraints(constraints)
//                        .setInputData(data.build())
//                        .build() // or PeriodicWorkRequest
//                    WorkManager.getInstance(context).enqueue(memberWorker)
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra("message", "There was no internet access! Data is saved locally")
//                    startActivity(intent)
//                }
            }
        }
    }
}
