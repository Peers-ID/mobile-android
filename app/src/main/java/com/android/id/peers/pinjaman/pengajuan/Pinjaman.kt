package com.android.id.peers.pinjaman.pengajuan

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import com.android.id.peers.anggota.StatusPinjaman
import com.android.id.peers.pinjaman.pencairan.PencairanPinjaman
import com.android.id.peers.util.callback.PencairanCallback
import com.android.id.peers.util.callback.PinjamanResponseCallback
import com.android.id.peers.util.callback.StatusPinjamanCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.id.peers.util.repository.ApiRepository
import com.android.id.peers.util.repository.CollectionRepository
import com.android.id.peers.util.repository.LoanRepository
import com.android.id.peers.util.response.ApiResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Parcelize
data class Pinjaman (
    var idProduct : Int = 0,
    var idMember : Int = 0,
    var jumlahPengajuan : Long = 0,
    var jumlahPencairan : Long = 0,
    var jumlahCicilan : Long = 0,
    var utangPokok : Long = 0,
    var bungaPinjaman : Long = 0
) : Parcelable {
    companion object {
        fun postPinjaman(preferences: SharedPreferences, context: Context, callback: PinjamanResponseCallback, pinjaman: Pinjaman) {
            val url = "${ApiConnections.API_HOSTNAME}loan/add"

            val params = HashMap<String, Any>()

            params["id_produk"] = pinjaman.idProduct
            params["id_member"] = pinjaman.idMember
            params["jumlah_pengajuan"] = pinjaman.jumlahPengajuan
            params["jumlah_pencairan"] = pinjaman.jumlahPencairan
            params["jumlah_cicilan"] = pinjaman.jumlahCicilan
            params["utang_pokok"] = pinjaman.utangPokok
            params["bunga_pinjaman"] = pinjaman.bungaPinjaman

            val token = "Bearer " + preferences.getString("token", null)!!

            val loanRepository = LoanRepository.create()

            loanRepository.add(token,params).enqueue(object : Callback<ApiResponse>{
                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: retrofit2.Response<ApiResponse>
                ) {
                    val res = response.body()

                    res.let {
                        val status = it!!.status
                        Log.d("postPinjaman", it.toString())
                        if (status == 201) {
                            val gson = Gson()
                            val data = JSONObject(gson.toJson(it!!.data))
                            val pinjamanResponse = PinjamanResponse()
                            pinjamanResponse.codePengajuan = data.getInt("code_pengajuan")
                            pinjamanResponse.descPengajuan = data.getString("desc_pengajuan")
                            pinjamanResponse.codePencairan = data.getInt("code_pencairan")
                            pinjamanResponse.descPencairan = data.getString("desc_pencairan")
                            pinjamanResponse.idLoan = data.getJSONObject("loan").getInt("id")
                            callback.onSuccess(pinjamanResponse)
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("postPinjaman", t.message)
                }

            })
        }

        fun getPinjamanMemberStatus(preferences: SharedPreferences, context: Context, callback: StatusPinjamanCallback) {
            val idKoperasi = preferences.getInt("koperasi_id", 0)
            val idAo = preferences.getInt("id", 0)

            val token = "Bearer " + preferences.getString("token", null)!!

            val loanRepository = LoanRepository.create()

            loanRepository.status(token,idKoperasi,idAo).enqueue(object : Callback<ApiResponse>{
                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: retrofit2.Response<ApiResponse>
                ) {
                    val res = response.body()

                    res.let{
                        val status = it!!.status
                        Log.d("PinjamanStatus", it.toString())

                        if (status == 200) {
                            val d = it!!.data as ArrayList<Any>
                            val data = JSONArray(d)
                            val statusList = ArrayList<StatusPinjaman>()
                            for (i in 0 until data.length()) {
                                val pinjamanJsonObject = data.getJSONObject(i)
                                val pinjamanMemberStatus = StatusPinjaman()
                                pinjamanMemberStatus.id = pinjamanJsonObject.getInt("id")
                                pinjamanMemberStatus.namaLengkap = pinjamanJsonObject.getString("nama_member")
                                pinjamanMemberStatus.status = pinjamanJsonObject.getString("nama_status")
                                val oldPattern = "yyyy-MM-dd"
                                val pattern = "dd MMM `yy"
                                val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
                                val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                                val oldDate: Date = oldDateFormat.parse(pinjamanJsonObject.getString("createdAt").substring(0, 10))!!
                                val date: String = simpleDateFormat.format(oldDate)
                                pinjamanMemberStatus.tanggal = date
                                statusList.add(pinjamanMemberStatus)
                            }

                            callback.onSuccess(statusList)
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("PinjamanStatus", t.message.toString())
                }

            })
        }

        fun getPencairanPinjaman(preferences: SharedPreferences, context: Context, callback: StatusPinjamanCallback) {
            val url = "${ApiConnections.API_HOSTNAME}loan"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("PencairanPinjaman", strResp)
                    if (status == 200) {
                        val data = jsonObj.getJSONArray("data")
                        val statusList = ArrayList<StatusPinjaman>()
                        for (i in 0 until data.length()) {
                            val pinjamanJsonObject = data.getJSONObject(i)
                            val pinjamanMemberStatus = StatusPinjaman()
                            pinjamanMemberStatus.id = pinjamanJsonObject.getInt("id")
                            pinjamanMemberStatus.namaLengkap = pinjamanJsonObject.getString("nama_member")
                            pinjamanMemberStatus.status = pinjamanJsonObject.getString("nama_status")
//                            var temp = LocalDateTime.parse(pinjamanJsonObject.getString("createdAt"))
//                            val oldPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                            val oldPattern = "yyyy-MM-dd"
                            val pattern = "dd MMM `yy"
                            val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
//                            oldDateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
                            val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                            val oldDate: Date = oldDateFormat.parse(pinjamanJsonObject.getString("createdAt").substring(0, 10))!!
                            val date: String = simpleDateFormat.format(oldDate)
                            pinjamanMemberStatus.tanggal = date
//                            Log.d("Pinjaman", "OLD DATE : ${pinjamanJsonObject.getString("createdAt").substring(0, 10)}")
//                            Log.d("Pinjaman", "NEW DATE : $date")
                            statusList.add(pinjamanMemberStatus)
                        }

                        callback.onSuccess(statusList)
                    }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("PencairanPinjaman", error.toString())
                }
            )
            {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        fun getDetailPencairan(preferences: SharedPreferences, context: Context, callback: PencairanCallback, idPinjaman: Int) {
            val url = "${ApiConnections.API_HOSTNAME}loan/detail/$idPinjaman"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("getDetailPencairan", strResp)
                    if (status == 200) {
                        val data = jsonObj.getJSONObject("data")
                        val pinjamanJsonObject = data.getJSONObject("loan")
                        val productJsonObject = data.getJSONObject("produk")
                        val memberJsonObject = data.getJSONObject("member")
                        val detailPencairan = PencairanPinjaman()
                        detailPencairan.idPinjaman = idPinjaman
                        detailPencairan.idMember = memberJsonObject.getInt("member_id")
                        detailPencairan.namaAnggota = memberJsonObject.getString("nama_lengkap")
                        detailPencairan.noKtp = memberJsonObject.getString("no_identitas")
                        val oldPattern = "yyyy-MM-dd"
                        val pattern = "dd MMMM yyyy"
                        val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
//                            oldDateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
                        val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                        val oldDate: Date = oldDateFormat.parse(pinjamanJsonObject.getString("createdAt").substring(0, 10))!!
                        val date: String = simpleDateFormat.format(oldDate)
                        detailPencairan.tanggalPengajuan = date
                        detailPencairan.namaProduct = pinjamanJsonObject.getString("nama_produk")
                        detailPencairan.jumlahPinjaman = pinjamanJsonObject.getLong("jumlah_pengajuan")
                        val simpananPokok = productJsonObject.getString("simpanan_pokok").replace(",", ".").toDouble()
                        val typeSimpananPokok = productJsonObject.getString("type_simpanan_pokok")
                        detailPencairan.simpananPokok = if (typeSimpananPokok.toLowerCase(Locale.ROOT) == "fix") simpananPokok.toLong() else (simpananPokok * detailPencairan.jumlahPinjaman / 100).toLong()
                        val admin = productJsonObject.getString("admin").replace(",", ".").toDouble()
                        val typeAdmin = productJsonObject.getString("type_admin")
                        Log.d("Pinjaman", "TYPE ADMIN : $typeAdmin, TYPE ADMIN(1) : ${typeAdmin.toLowerCase(Locale.ROOT)}, ADMIN : $admin, BIAYA ADMIN : ${admin * detailPencairan.jumlahPinjaman / 100}")
                        val biayaAdmin = if (typeAdmin.toLowerCase(Locale.ROOT) == "fix") admin.toLong() else (admin * detailPencairan.jumlahPinjaman / 100).toLong()
                        detailPencairan.biayaAdmin = biayaAdmin
                        detailPencairan.jumlahPencairan = pinjamanJsonObject.getLong("jumlah_pencairan")
                        detailPencairan.cicilan = pinjamanJsonObject.getLong("jumlah_cicilan")
                        detailPencairan.tenor = productJsonObject.getInt("tenor")
                        detailPencairan.satuanTenor = productJsonObject.getString("satuan_tenor")

                        var asuransi = if(!productJsonObject.isNull("asuransi")) productJsonObject.getString("asuransi").replace(",", ".").toDouble() else 0.0
                        var typeAsuransi = if(!productJsonObject.isNull("type_asuransi")) productJsonObject.getString("type_asuransi") else ""
                        var jpk = if(!productJsonObject.isNull("dana_jpk")) productJsonObject.getString("dana_jpk").replace(",", ".").toDouble() else 0.0
                        var typeJpk = if(!productJsonObject.isNull("type_dana_jpk")) productJsonObject.getString("type_dana_jpk") else ""

                        var provisi = if(!productJsonObject.isNull("provisi")) productJsonObject.getString("provisi").replace(",", ".").toDouble() else 0.0
                        var typeProvisi = if(!productJsonObject.isNull("type_provisi")) productJsonObject.getString("type_provisi") else ""

                        val biayaAsuransi : Long = if (typeAsuransi.toLowerCase(Locale.ROOT) == "fix") asuransi.toLong() else asuransi.toLong() * detailPencairan.jumlahPinjaman / 100
                        val danaJpk : Long = if (typeJpk.toLowerCase(Locale.ROOT) == "fix") jpk.toLong() else jpk.toLong() * detailPencairan.jumlahPinjaman / 100

                        val biayaProvisi : Long = if (typeProvisi.toLowerCase(Locale.ROOT) == "fix") provisi.toLong() else provisi.toLong() * detailPencairan.jumlahPinjaman / 100

                        detailPencairan.asuransi = biayaAsuransi
                        detailPencairan.jpk = danaJpk
                        detailPencairan.provisi = biayaProvisi

                        callback.onSuccess(detailPencairan)
                    }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("getDetailPencairan", error.toString())
                }
            )
            {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        fun putStatusPencairan(preferences: SharedPreferences, context: Context, idMember: Int, idPinjaman: Int) {
            val url = "${ApiConnections.API_HOSTNAME}loan/status"

            val params = HashMap<String, Any>()

            params["id_member"] = idMember
            params["id_loan"] = idPinjaman
            params["id_status"] = 1

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.PUT, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
//                    val jsonObj = JSONObject(strResp)
//                    val loginStatus = jsonObj.getInt("status")
                    Log.d("StatusPencairan", strResp)
//                    if (loginStatus == 201) {
//                        val data = jsonObj.getJSONObject("data")
//                        val pinjamanResponse = PinjamanResponse()
//                        pinjamanResponse.codePengajuan = data.getInt("code_pengajuan")
//                        pinjamanResponse.descPengajuan = data.getString("desc_pengajuan")
//                        pinjamanResponse.codePencairan = data.getInt("code_pencairan")
//                        pinjamanResponse.descPencairan = data.getString("desc_pencairan")
//                        callback.onSuccess(pinjamanResponse)
//                    }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("putStatusPencairan", error.toString())
                }
            )
            {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    //..add other headers
                    return headers
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        fun getPembayaranCicilan(preferences: SharedPreferences, context: Context, callback: StatusPinjamanCallback) {
            val token = "Bearer " + preferences.getString("token", null)!!

            val collectionRepository = CollectionRepository.create()

            collectionRepository.get(token).enqueue(object: Callback<ApiResponse>{
                override fun onResponse(
                    call: Call<ApiResponse>,
                    response: retrofit2.Response<ApiResponse>
                ) {
                    val res = response.body()

                    res.let {
                        val status = it!!.status

                        Log.d("pembayaranCicilan", it.toString())

                        if (status == 200) {
                            val d = it!!.data as ArrayList<Any>
                            var data = JSONArray(d)

                            val statusList = ArrayList<StatusPinjaman>()
                            for (i in 0 until data.length()) {
                                val pinjamanJsonObject = data.getJSONObject(i)
                                val pinjamanMemberStatus = StatusPinjaman()
                                pinjamanMemberStatus.id = pinjamanJsonObject.getInt("id")
                                pinjamanMemberStatus.idMember = pinjamanJsonObject.getInt("id_member")
                                pinjamanMemberStatus.idLoan = pinjamanJsonObject.getInt("id_loan")
                                pinjamanMemberStatus.angsuran = pinjamanJsonObject.getInt("angsuran")
                                Log.d("pembayaranCicilan", "Member ID : ")
                                pinjamanMemberStatus.namaLengkap = pinjamanJsonObject.getString("nama_lengkap")
                                val oldPattern = "yyyy-MM-dd"
                                val pattern = "dd MMMM yyyy"
                                val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
                                val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                                val oldDate: Date = oldDateFormat.parse(pinjamanJsonObject.getString("loan_due_date").substring(0, 10))!!
                                val date: String = simpleDateFormat.format(oldDate)
                                pinjamanMemberStatus.tanggal = date
                                statusList.add(pinjamanMemberStatus)
                            }

                            callback.onSuccess(statusList)
                        }
                    }

                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("pembayaranCicilan", t.message.toString())
                }

            })
        }
    }
}