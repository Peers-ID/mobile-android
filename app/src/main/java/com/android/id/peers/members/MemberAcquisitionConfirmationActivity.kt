package com.android.id.peers.members

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.id.peers.R
import com.android.id.peers.TermsActivity
import com.android.id.peers.loans.LoanApplicationActivity
import com.android.id.peers.members.models.Member

class MemberAcquisitionConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_acquisition_confirmation)

        val identityType = findViewById<TextView>(R.id.identity_type)
        val identityNo = findViewById<TextView>(R.id.identity_no)
        val fullName = findViewById<TextView>(R.id.full_name)
        val handphoneNo = findViewById<TextView>(R.id.handphone_no)
        val birthDate = findViewById<TextView>(R.id.birth_date)
        val birthPlace = findViewById<TextView>(R.id.birth_place)
        val sex = findViewById<TextView>(R.id.sex)
        val motherName = findViewById<TextView>(R.id.mother_name)
        val maritalStatus = findViewById<TextView>(R.id.marital_status)
        val lastEducation = findViewById<TextView>(R.id.last_education)

        val addressStreet = findViewById<TextView>(R.id.address_street)
        val addressNo = findViewById<TextView>(R.id.address_no)
        val addressRt = findViewById<TextView>(R.id.address_rt)
        val addressRw = findViewById<TextView>(R.id.address_rw)
        val addressKelurahan = findViewById<TextView>(R.id.address_kelurahan)
        val addressKecamatan = findViewById<TextView>(R.id.address_kecamatan)
        val addressCity = findViewById<TextView>(R.id.address_city)
        val addressStatus = findViewById<TextView>(R.id.address_status)
        val addressHowLong = findViewById<TextView>(R.id.address_how_long)

        val addressStreetDomisili = findViewById<TextView>(R.id.address_street_domisili)
        val addressNoDomisili = findViewById<TextView>(R.id.address_no_domisili)
        val addressRtDomisili = findViewById<TextView>(R.id.address_rt_domisili)
        val addressRwDomisili = findViewById<TextView>(R.id.address_rw_domisili)
        val addressKelurahanDomisili = findViewById<TextView>(R.id.address_kelurahan_domisili)
        val addressKecamatanDomisili = findViewById<TextView>(R.id.address_kecamatan_domisili)
        val addressCityDomisili = findViewById<TextView>(R.id.address_city_domisili)
        val addressStatusDomisili = findViewById<TextView>(R.id.address_status_domisili)
        val addressHowLongDomisili = findViewById<TextView>(R.id.address_how_long_domisili)

        val npwpExist = findViewById<TextView>(R.id.npwp_exist)
        val npwpNo = findViewById<TextView>(R.id.npwp_no)
        val occupationStatus = findViewById<TextView>(R.id.occupation_status)
        val occupationField = findViewById<TextView>(R.id.occupation_field)
        val occupationPosition = findViewById<TextView>(R.id.occupation_position)
        val companyName = findViewById<TextView>(R.id.company_name)
        val occupationHowLong = findViewById<TextView>(R.id.work_how_long)
        val occupationRevenue = findViewById<TextView>(R.id.occupation_revenue)
        val officeAddressStreet = findViewById<TextView>(R.id.office_address_street)
        val officeAddressNo = findViewById<TextView>(R.id.office_address_no)
        val officeAddressRt = findViewById<TextView>(R.id.office_address_rt)
        val officeAddressRw = findViewById<TextView>(R.id.office_address_rw)
        val officeAddressKelurahan = findViewById<TextView>(R.id.office_address_kelurahan)
        val officeAddressKecamatan = findViewById<TextView>(R.id.office_address_kecamatan)
        val officeAddressCity = findViewById<TextView>(R.id.office_address_city)

        val emergencyName = findViewById<TextView>(R.id.emergency_name)
        val emergencyHandphoneNo = findViewById<TextView>(R.id.emergency_handphone_no)
        val relationship = findViewById<TextView>(R.id.relationship)

        val submitButton = findViewById<Button>(R.id.submit_button)

        val member = intent.getParcelableExtra<Member>("member")

        if(member != null) {
            when(member.jenisIdentitas) {
                0 -> identityType.text = "KTP"
                1 -> identityType.text = "SIM"
                2 -> identityType.text = "Paspor"
            }
            identityNo.text = member.noIdentitas
            fullName.text = member.namaLengkap
            handphoneNo.text = member.noHp
            birthDate.text = member.tanggalLahir
            birthPlace.text = member.tempatLahir
            when(member.jenisKelamin) {
                0 -> sex.text = "Pria"
                1 -> sex.text = "Wanita"
            }
            motherName.text = member.namaGadisIbuKandung
            when(member.statusPernikahan) {
                0 -> maritalStatus.text = "Kawin"
                1 -> maritalStatus.text = "Belum Kawin"
                2 -> maritalStatus.text = "Cerai"
            }
            when(member.pendidikanTerakhir) {
                0 -> lastEducation.text = "SD"
                1 -> lastEducation.text = "SMP"
                2 -> lastEducation.text = "SMA"
                3 -> lastEducation.text = "D3"
                4 -> lastEducation.text = "S1"
                5 -> lastEducation.text = "Lainnya"
            }

            addressStreet.text = member.jalanSesuaiKtp
            addressNo.text = member.nomorSesuaiKtp
            addressRt.text = member.rtSesuaiKtp
            addressRw.text = member.rwSesuaiKtp
            addressKelurahan.text = member.kelurahanSesuaiKtp
            addressKecamatan.text = member.kecamatanSesuaiKtp
            addressCity.text = member.kotaSesuaiKtp
            when(member.statusTempatTinggalSesuaiKtp) {
                0 -> addressStatus.text = "Milik Sendiri"
                1 -> addressStatus.text = "Milik Keluarga"
                2 -> addressStatus.text = "Rumah Dinas"
                3 -> addressStatus.text = "Sewa / Kontrak"
                4 -> addressStatus.text = "Kost"
            }
            val addressHowLongText = String.format("%d", member.lamaBulanTinggalSesuaiKtp) + " bulan, " + String.format("%d", member.lamaTahunTinggalSesuaiKtp) + " tahun"
            addressHowLong.text = addressHowLongText

            addressStreetDomisili.text = member.jalanDomisili
            addressNoDomisili.text = member.nomorDomisili
            addressRtDomisili.text = member.rtDomisili
            addressRwDomisili.text = member.rwDomisili
            addressKelurahanDomisili.text = member.kelurahanDomisili
            addressKecamatanDomisili.text = member.kecamatanDomisili
            addressCityDomisili.text = member.kotaDomisili
            when(member.statusTempatTinggalDomisili) {
                0 -> addressStatusDomisili.text = "Milik Sendiri"
                1 -> addressStatusDomisili.text = "Milik Keluarga"
                2 -> addressStatusDomisili.text = "Rumah Dinas"
                3 -> addressStatusDomisili.text = "Sewa / Kontrak"
                4 -> addressStatusDomisili.text = "Kost"
            }
            val addressHowLongDomisiliText = String.format("%d", member.lamaBulanTinggalDomisili) + " bulan, " + String.format("%d", member.lamaTahunTinggalDomisili) + " tahun"
            addressHowLongDomisili.text = addressHowLongDomisiliText

            when(member.memilikiNpwp) {
                0 -> npwpExist.text = "Ada"
                1 -> npwpExist.text = "Tidak Ada"
            }
            npwpNo.text = member.nomorNpwp
            when(member.pekerjaan) {
                0 -> occupationStatus.text = "Pelajar / Mahasiswa"
                1 -> occupationStatus.text = "Pegawai Swasta"
                2 -> occupationStatus.text = "Pegawai Negeri"
                3 -> occupationStatus.text = "Guru / Dosen"
                4 -> occupationStatus.text = "TNI / POLRI"
                5 -> occupationStatus.text = "Pensiunan"
                6 -> occupationStatus.text = "Wiraswasta"
                7 -> occupationStatus.text = "Profesional Lainnya"
                8 -> occupationStatus.text = "Ibu Rumah Tangga"
            }
            occupationField.text = member.bidangPekerjaan
            occupationPosition.text = member.posisiPekerjaan
            companyName.text = member.namaPerusahaan
            val occupationHowLongText = String.format("%d", member.lamaBulanBekerja) + " bulan, " + String.format("%d", member.lamaTahunBekerja) + " tahun"
            occupationHowLong.text = occupationHowLongText
            occupationRevenue.text = member.penghasilan
            officeAddressStreet.text = member.jalanKantor
            officeAddressNo.text = member.nomorKantor
            officeAddressRt.text = member.rtKantor
            officeAddressRw.text = member.rwKantor
            officeAddressKelurahan.text = member.kelurahanKantor
            officeAddressKecamatan.text = member.kecamatanKantor
            officeAddressCity.text = member.kotaKantor

            emergencyName.text = member.namaEmergency
            emergencyHandphoneNo.text = member.noHpEmergency
            when(member.hubunganEmergeny) {
                0 -> relationship.text = "Suami/Istri"
                1 -> relationship.text = "Saudara Kandung"
                2 -> relationship.text = "Anak"
                3 -> relationship.text = "Orang Tua"
                4 -> relationship.text = "Lainnya"
            }

            submitButton.setOnClickListener { submitData(it) }
        }
    }

    fun submitData(view: View) {
        //submit Data to Server here

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
        val intent = Intent(this, LoanApplicationActivity::class.java)
        startActivity(intent)
    }

    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        val intent = Intent(this, TermsActivity::class.java)
        val handphoneNo = findViewById<TextView>(R.id.handphone_no)
        intent.putExtra("hand_phone", handphoneNo.text.toString())
        startActivity(intent)
    }
}
