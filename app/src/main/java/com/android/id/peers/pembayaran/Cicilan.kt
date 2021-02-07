package com.android.id.peers.pembayaran

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.id.peers.util.callback.CicilanCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Cicilan {
    var idLoan : Int = 0
    var idAnggota : Int = 0
    var namaAnggota : String = ""
    var namaProduct : String = ""
    var sisaPinjaman : Long = 0
    var jatuhTempo : String = ""
    var pokok : Long = 0
    var bunga : Long = 0
    var angsuran : Int = 0
    var pembayaranKe : Int = 0
    var totalTagihan : Long = 0
    var simpananWajib : Long = 0
    var simpananSukarela : Long = 0
    var denda : Long = 0
    var totalPembayaran : Long = 0
    var jumlahSetoran : Long = 0
    var bayarDenganSimpanan : Int = 0

    companion object {
        fun getDetailCicilan(preferences: SharedPreferences, context: Context, callback: CicilanCallback, idMember: Int, angsuran: Int) {
            val url = "${ApiConnections.API_HOSTNAME}collection/member"

            Log.d("getDetailCicilan", "ID MEMBER : $idMember")

            val params = HashMap<String, Any>()
            params["id_member"] = idMember
            params["angsuran"] = angsuran
            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("getDetailCicilan", strResp)
                    if (status == 200) {
                        val data = jsonObj.getJSONArray("data")

                        val cicilan = Cicilan()
                        val cicilanJsonObject = data.getJSONObject(data.length() - 1)
                        cicilan.idLoan = cicilanJsonObject.getInt("id_loan")
                        cicilan.idAnggota = cicilanJsonObject.getInt("id_member")
                        cicilan.namaAnggota = cicilanJsonObject.getString("nama_lengkap")
                        cicilan.namaProduct = cicilanJsonObject.getString("nama_produk")
                        val oldPattern = "yyyy-MM-dd"
                        val pattern = "dd MMM yyyy"
                        val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
//                            oldDateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
                        val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                        val oldDate: Date = oldDateFormat.parse(cicilanJsonObject.getString("loan_due_date").substring(0, 10))!!
                        val date: String = simpleDateFormat.format(oldDate)
                        cicilan.jatuhTempo = date
                        cicilan.totalTagihan = cicilanJsonObject.getLong("total_tagihan")
                        cicilan.simpananWajib = cicilanJsonObject.getLong("simpanan_wajib")
                        cicilan.denda = cicilanJsonObject.getLong("denda")
                        cicilan.pokok = cicilanJsonObject.getLong("pokok")
                        cicilan.bunga = cicilanJsonObject.getLong("bunga")
                        cicilan.angsuran = cicilanJsonObject.getInt("angsuran")
                        cicilan.pembayaranKe = cicilanJsonObject.getInt("pembayaran_ke")
                        cicilan.totalPembayaran = cicilan.totalTagihan + cicilan.simpananWajib + cicilan.denda

                        callback.onSuccess(cicilan)
                    }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("postMember", error.toString())
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

        fun postPembayaranCicilan(preferences: SharedPreferences, context: Context, cicilan: Cicilan) {
            val url = "${ApiConnections.API_HOSTNAME}collection/add"

            val params = HashMap<String, Any>()
            params["id_loan"] = cicilan.idLoan
            params["id_member"] = cicilan.idAnggota
            params["angsuran"] = cicilan.angsuran
            params["pembayaran_ke"] = cicilan.pembayaranKe
            params["utang_pokok"] = cicilan.pokok
            params["bunga_pinjaman"] = cicilan.bunga
            params["simpanan_wajib"] = cicilan.simpananWajib
            params["simpanan_sukarela"] = cicilan.simpananSukarela
            params["denda"] = cicilan.denda
            params["setoran"] = cicilan.jumlahSetoran
            params["bayar_dengan_simpanan"] = cicilan.bayarDenganSimpanan
            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
//                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                    memberPreferences.edit().putString("no_hp", member.noHp).apply()
//                    val jsonObj = JSONObject(strResp)
//                    val status = jsonObj.getInt("status")
                    Log.d("postPembayaranCicilan", strResp)
//                    if (status == 200) {
//                        val data = jsonObj.getJSONArray("data")
//
//                        val cicilan = Cicilan()
//                        val cicilanJsonObject = data.getJSONObject(data.length() - 1)
//                        cicilan.idAnggota = cicilanJsonObject.getInt("id_member")
//                        cicilan.namaAnggota = cicilanJsonObject.getString("nama_lengkap")
//                        cicilan.namaProduct = cicilanJsonObject.getString("nama_produk")
//                        val oldPattern = "yyyy-MM-dd"
//                        val pattern = "dd MMM yyyy"
//                        val oldDateFormat = SimpleDateFormat(oldPattern, Locale.US)
////                            oldDateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
//                        val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
//                        val oldDate: Date = oldDateFormat.parse(cicilanJsonObject.getString("loan_due_date").substring(0, 10))!!
//                        val date: String = simpleDateFormat.format(oldDate)
//                        cicilan.jatuhTempo = date
//                        cicilan.totalTagihan = cicilanJsonObject.getLong("total_tagihan")
//                        cicilan.simpananWajib = cicilanJsonObject.getLong("simpanan_wajib")
//                        cicilan.denda = cicilanJsonObject.getLong("denda")
//                        cicilan.pokok = cicilanJsonObject.getLong("pokok")
//                        cicilan.bunga = cicilanJsonObject.getLong("bunga")
//                        cicilan.angsuran = cicilanJsonObject.getInt("angsuran")
//                        cicilan.pembayaranKe = cicilanJsonObject.getInt("pembayaran_ke")
//                        cicilan.totalPembayaran = cicilan.totalTagihan + cicilan.simpananWajib + cicilan.denda
//
//                        callback.onSuccess(cicilan)
//                    }
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }

                },
                Response.ErrorListener { error ->
                    Log.e("postMember", error.toString())
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
    }
}