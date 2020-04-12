package com.android.id.peers.members

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.android.id.peers.R
import com.android.id.peers.TermsActivity
import com.android.id.peers.loans.LoanApplicationActivity
import com.android.id.peers.members.models.Member
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.NetworkConnectivity
import kotlinx.android.synthetic.main.activity_member_acquisition_confirmation.*
import kotlinx.android.synthetic.main.layout_address.*
import kotlinx.android.synthetic.main.layout_address_domisili.*
import kotlinx.android.synthetic.main.layout_emergency_contact.*
import kotlinx.android.synthetic.main.layout_occupation.*
import kotlinx.android.synthetic.main.layout_personal_information.*
import kotlinx.android.synthetic.main.layout_occupation.occupation_position
import kotlinx.android.synthetic.main.layout_office_address.*

class MemberAcquisitionConfirmationActivity : AppCompatActivity() {
    var sesuai : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_acquisition_confirmation)

        val member = intent.getParcelableExtra<Member>("member")
        if(member != null) {
            identity_type.text = member.memberJenisIdentitasString()
            identity_no.text = member.noIdentitas
            full_name.text = member.namaLengkap
            handphone_no.text = member.noHp
            birth_date.text = member.tanggalLahir
            birth_place.text = member.tempatLahir
            sex.text = member.jenisKelaminString()
            mother_name.text = member.namaGadisIbuKandung
            marital_status.text = member.statusPerkawinanString()
            last_education.text = member.pendidikanTerakhirString()

            address_street.text = member.jalanSesuaiKtp
            address_no.text = member.nomorSesuaiKtp
            address_rt.text = member.rtSesuaiKtp
            address_rw.text = member.rwSesuaiKtp
            address_kelurahan.text = member.kelurahanSesuaiKtp
            address_kecamatan.text = member.kecamatanSesuaiKtp
            address_city.text = member.kotaSesuaiKtp
            address_status.text = member.statusTempatTinggalSesuaiKtpString()
            val addressHowLongText = String.format("%d", member.lamaBulanTinggalSesuaiKtp - 1) + " bulan, " + String.format("%d", member.lamaTahunTinggalSesuaiKtp - 1) + " tahun"
            address_how_long.text = addressHowLongText
            sesuai = member.domisiliSesuaiKtp

            address_street_domisili.text = member.jalanDomisili
            address_no_domisili.text = member.nomorDomisili
            address_rt_domisili.text = member.rtDomisili
            address_rw_domisili.text = member.rwDomisili
            address_kelurahan_domisili.text = member.kelurahanDomisili
            address_kecamatan_domisili.text = member.kecamatanDomisili
            address_city_domisili.text = member.kotaDomisili
            address_status_domisili.text = member.statusTempatTinggalDomisiliString()
            val addressHowLongDomisiliText = String.format("%d", member.lamaBulanTinggalDomisili - 1) + " bulan, " + String.format("%d", member.lamaTahunTinggalDomisili - 1) + " tahun"
            address_how_long_domisili.text = addressHowLongDomisiliText

            npwp_exist.text = member.memilikiNpwpString()
            npwp_no.text = member.nomorNpwp
            occupation_status.text = member.pekerjaanString()
            occupation_field.text = member.bidangPekerjaan
            occupation_position.text = member.posisiPekerjaan
            company_name.text = member.namaPerusahaan
            val occupationHowLongText = String.format("%d", member.lamaBulanBekerja - 1) + " bulan, " + String.format("%d", member.lamaTahunBekerja - 1) + " tahun"
            work_how_long.text = occupationHowLongText
            occupation_revenue.text = member.penghasilan.toString()
            office_address_street.text = member.jalanKantor
            office_address_no.text = member.nomorKantor
            office_address_rt.text = member.rtKantor
            office_address_rw.text = member.rwKantor
            office_address_kelurahan.text = member.kelurahanKantor
            office_address_kecamatan.text = member.kecamatanKantor
            office_address_city.text = member.kotaKantor

            emergency_name.text = member.namaEmergency
            emergency_handphone_no.text = member.noHpEmergency
            relationship.text = member.hubunganEmergencyString()

            submit_button.setOnClickListener { submitData(it, member) }
        }
    }

    fun submitData(view: View, member: Member) {
        //submit Data to Server here
        val networkConnectivity = NetworkConnectivity(this)
        if(networkConnectivity.isConnectedOverWifi()) {
            networkConnectivity.deleteCache()
        }

        if (networkConnectivity.isNetworkConnected()) {
            val apiConnections = ApiConnections()
            apiConnections.authenticate(getSharedPreferences("login_data", Context.MODE_PRIVATE),
                this, ApiConnections.REQUEST_TYPE_POST_MEMBER, member)
        }

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
        intent.putExtra("member_handphone", member.noHp)
        intent.putExtra("member_name", member.namaLengkap)
        finishAffinity()
        startActivity(intent)
    }

    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        val intent = Intent(this, TermsActivity::class.java)
        startActivity(intent)
    }
}
