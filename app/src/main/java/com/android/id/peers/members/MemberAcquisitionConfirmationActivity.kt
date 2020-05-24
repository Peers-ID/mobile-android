package com.android.id.peers.members

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.id.peers.R
import com.android.id.peers.TermsActivity
import com.android.id.peers.loans.LoanApplicationActivity
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.CurrencyFormat
import kotlinx.android.synthetic.main.activity_member_acquisition_confirmation.*
import kotlinx.android.synthetic.main.layout_address.*
import kotlinx.android.synthetic.main.layout_address_domisili.*
import kotlinx.android.synthetic.main.layout_emergency_contact.*
import kotlinx.android.synthetic.main.layout_occupation.*
import kotlinx.android.synthetic.main.layout_office_address.*
import kotlinx.android.synthetic.main.layout_personal_information.*

class MemberAcquisitionConfirmationActivity : AppCompatActivity() {
    var sesuai : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_acquisition_confirmation)

        val member = intent.getParcelableExtra<Member>("member")
        if(member != null) {
            identity_type.text = if (member.memberJenisIdentitasString().isNotEmpty()) member.memberJenisIdentitasString() else "-"
            identity_no.text = if (member.noIdentitas.isNotEmpty()) member.noIdentitas else "-"
            full_name.text = if (member.namaLengkap.isNotEmpty()) member.namaLengkap else "-"
            handphone_no.text = if (member.noHp.isNotEmpty()) member.noHp else "-"
            birth_date.text = if(member.tanggalLahir.isNotEmpty()) member.tanggalLahir else "-"
            birth_place.text = if(member.tempatLahir.isNotEmpty()) member.tempatLahir else "-"
            sex.text = if(member.jenisKelaminString().isNotEmpty()) member.jenisKelaminString() else "-"
            mother_name.text = if(member.namaGadisIbuKandung.isNotEmpty()) member.namaGadisIbuKandung else "-"
            marital_status.text = if(member.statusPerkawinanString().isNotEmpty()) member.statusPerkawinanString() else "-"
            last_education.text = if (member.pendidikanTerakhirString().isNotEmpty()) member.pendidikanTerakhirString() else "-"

            address_street.text = if (member.jalanSesuaiKtp.isNotEmpty()) member.jalanSesuaiKtp else "-"
            address_no.text = if (member.nomorSesuaiKtp.isNotEmpty()) member.nomorSesuaiKtp else "-"
            address_rt.text = if (member.rtSesuaiKtp.isNotEmpty()) member.rtSesuaiKtp else "-"
            address_rw.text = if (member.rwSesuaiKtp.isNotEmpty()) member.rwSesuaiKtp else "-"
            address_kelurahan.text = if (member.kelurahanSesuaiKtp.isNotEmpty()) member.kelurahanSesuaiKtp else "-"
            address_kecamatan.text = if (member.kecamatanSesuaiKtp.isNotEmpty()) member.kecamatanSesuaiKtp else "-"
            address_city.text = if (member.kotaSesuaiKtp.isNotEmpty()) member.kotaSesuaiKtp else "-"
            address_status.text = if (member.statusTempatTinggalSesuaiKtpString().isNotEmpty()) member.statusTempatTinggalSesuaiKtpString() else "-"
            if (member.lamaBulanBekerja != -1 && member.lamaTahunBekerja != -1) {
                val addressHowLongText = String.format(
                    "%d",
                    member.lamaBulanTinggalSesuaiKtp - 1
                ) + " bulan, " + String.format(
                    "%d",
                    member.lamaTahunTinggalSesuaiKtp - 1
                ) + " tahun"
                address_how_long.text = addressHowLongText
            } else {
                address_how_long.text = "-"
            }
            sesuai = member.domisiliSesuaiKtp

            address_street_domisili.text = if (member.jalanDomisili.isNotEmpty()) member.jalanDomisili else "-"
            address_no_domisili.text = if (member.nomorDomisili.isNotEmpty()) member.nomorDomisili else "-"
            address_rt_domisili.text = if(member.rtDomisili.isNotEmpty()) member.rtDomisili else "-"
            address_rw_domisili.text = if(member.rwDomisili.isNotEmpty()) member.rwDomisili else "-"
            address_kelurahan_domisili.text = if(member.kelurahanDomisili.isNotEmpty()) member.kelurahanDomisili else "-"
            address_kecamatan_domisili.text = if(member.kecamatanDomisili.isNotEmpty()) member.kecamatanDomisili else "-"
            address_city_domisili.text = if(member.kotaDomisili.isNotEmpty()) member.kotaDomisili else "-"
            address_status_domisili.text = if(member.statusTempatTinggalDomisiliString().isNotEmpty()) member.statusTempatTinggalDomisiliString() else "-"
            if (member.lamaBulanBekerja != -1 && member.lamaTahunBekerja != -1) {
                val addressHowLongDomisiliText = String.format(
                    "%d",
                    member.lamaBulanTinggalDomisili - 1
                ) + " bulan, " + String.format("%d", member.lamaTahunTinggalDomisili - 1) + " tahun"
                address_how_long_domisili.text = addressHowLongDomisiliText
            } else {
                address_how_long_domisili.text = "-"
            }

            npwp_exist.text = if (member.memilikiNpwpString().isNotEmpty()) member.memilikiNpwpString() else "-"
            npwp_no.text = if (member.nomorNpwp.isNotEmpty()) member.nomorNpwp else "-"
            occupation_status.text = if (member.pekerjaanString().isNotEmpty()) member.pekerjaanString() else "-"
            occupation_field.text = if (member.bidangPekerjaan.isNotEmpty()) member.bidangPekerjaan else "-"
            occupation_position.text = if (member.posisiPekerjaan.isNotEmpty()) member.posisiPekerjaan else "-"
            company_name.text = if (member.namaPerusahaan.isNotEmpty()) member.namaPerusahaan else "-"
            if (member.lamaBulanBekerja != -1 && member.lamaTahunBekerja != -1) {
                val occupationHowLongText = String.format("%d", member.lamaBulanBekerja - 1) + " bulan, " + String.format("%d", member.lamaTahunBekerja - 1) + " tahun"
                work_how_long.text = occupationHowLongText
            } else {
                work_how_long.text = "-"
            }
            occupation_revenue.text = CurrencyFormat.formatRupiah.format(member.penghasilan).toString()
            office_address_street.text = if(member.jalanKantor.isNotEmpty()) member.jalanKantor else "-"
            office_address_no.text = if(member.nomorKantor.isNotEmpty()) member.nomorKantor else "-"
            office_address_rt.text = if(member.rtKantor.isNotEmpty()) member.rtKantor else "-"
            office_address_rw.text = if(member.rwKantor.isNotEmpty()) member.rwKantor else "-"
            office_address_kelurahan.text = if(member.kelurahanKantor.isNotEmpty()) member.kelurahanKantor else "-"
            office_address_kecamatan.text = if(member.kecamatanKantor.isNotEmpty()) member.kecamatanKantor else "-"
            office_address_city.text = if(member.kotaKantor.isNotEmpty()) member.kotaKantor else "-"
            office_address_province.text = if(member.provinsiKantor.isNotEmpty()) member.provinsiKantor else "-"

            emergency_name.text = if(member.namaEmergency.isNotEmpty()) member.namaEmergency else "-"
            emergency_handphone_no.text = if(member.namaEmergency.isNotEmpty()) member.noHpEmergency else "-"
            relationship.text = if(member.hubunganEmergencyString().isNotEmpty()) member.hubunganEmergencyString() else "-"

            submit_button.setOnClickListener { submitData(it, member) }
        }
    }

    fun submitData(view: View, member: Member) {
        //submit Data to Server here
//        val networkConnectivity = NetworkConnectivity(this)
//        if(networkConnectivity.isConnectedOverWifi()) {
//            networkConnectivity.deleteCache()
//        }

//        if (networkConnectivity.isNetworkConnected()) {

//        val connectionStateMonitor = ConnectionStateMonitor(application)
//        connectionStateMonitor.observe(this, Observer { isConnected ->
//            if (isConnected) {
//                val apiConnections = ApiConnections()
//                apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
//                    this, ApiConnections.REQUEST_TYPE_POST_MEMBER, member)
//            } else {
//                val offlineViewModel: OfflineViewModel = ViewModelProvider(this).get(
//                    OfflineViewModel::class.java)
//                offlineViewModel.insertMember(member)
//            }
//        })

//        } else {
//            val memberDao = OfflineDatabase.getDatabase(application).memberDao()
//            memberDao.insertAll(member)
//        }

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Konfirmasi")
            setMessage("Data telah berhasil disimpan.\nMau mengajukan pinjaman?")
            setPositiveButton("Yes", positiveButtonClick)
            setNegativeButton("No", negativeButtonClick)
            show()
        }
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        val member = intent.getParcelableExtra<Member>("member")!!
        val intent = Intent(this, LoanApplicationActivity::class.java)
        intent.putExtra("member", member)
        intent.putExtra("member_handphone", member.noHp)
        intent.putExtra("member_name", member.namaLengkap)
        finishAffinity()
        startActivity(intent)
    }

    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        val member = intent.getParcelableExtra<Member>("member")!!
        val intent = Intent(this, TermsActivity::class.java)
        intent.putExtra("member", member)
        intent.putExtra("member_handphone", member.noHp)
        intent.putExtra("member_name", member.namaLengkap)
        startActivity(intent)
    }
}
