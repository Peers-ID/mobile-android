package com.android.id.peers.simpanan

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.id.peers.members.models.Member
import com.android.id.peers.members.models.MemberNikStatus
import com.android.id.peers.util.callback.SimpananItemCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

data class Simpanan (
    var namaProduct : String = "",
    var namaMember : String = "",
    var statusPinjaman : String = "",
    var simpananPokok : Long = 0,
    var simpananWajib : Long = 0,
    var simpananSukarela : Long = 0,
    var totalSimpanan : Long = 0,
    var length : Int = 0
) {
    companion object {
        fun getSimpanan(preferences: SharedPreferences, context: Context, idMember: Int, callback: SimpananItemCallback, length: Int) {
            val url = "${ApiConnections.API_HOSTNAME}loan/simpanan/$idMember/total"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("getSimpanan", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status == 200) {
                        val data = jsonObj.getJSONObject("data")
                        val loans = data.getJSONArray("loan")
                        val simpanans = ArrayList<Simpanan>()
                        if (loans.length() > 0) {
                            for (i in 0 until loans.length()) {
                                val loan = loans.getJSONObject(i)
                                val simpanan = Simpanan()
                                simpanan.length = length
                                simpanan.namaProduct = loan.getString("nama_produk")
                                simpanan.namaMember = loan.getString("nama_member")
                                simpanan.statusPinjaman = loan.getString("desc_status")
                                val simpananPokok = loan.getJSONArray("SimpananPokok")
                                val simpananWajib = loan.getJSONArray("SimpananWajib")
                                val simpananSukarela = loan.getJSONArray("SimpananSukarela")
                                simpanan.simpananPokok = if (simpananPokok.length() > 0 ) simpananPokok.getJSONObject(0).getLong("total_simpanan") else 0
                                simpanan.simpananWajib = if (simpananWajib.length() > 0 ) simpananWajib.getJSONObject(0).getLong("total_simpanan") else 0
                                simpanan.simpananSukarela = if (simpananSukarela.length() > 0 ) simpananSukarela.getJSONObject(0).getLong("total_simpanan") else 0
                                simpanan.totalSimpanan = simpanan.simpananPokok + simpanan.simpananWajib + simpanan.simpananSukarela
                                simpanans.add(simpanan)
                            }
                        } else {
                            val simpanan = Simpanan()
                            simpanan.length = length
                            simpanans.add(simpanan)
                        }

                        Log.d("getSimpanan", "SIZE SIMPANAN : ${simpanans.size}")
                        callback.onSuccess(simpanans)
                    }
                },
                Response.ErrorListener { error ->
                    //                Log.e("getConfig", error.toString())
                    val response = error.networkResponse
                    if (error is ServerError && response != null) {
                        try {
                            val res = String(
                                response.data,
                                Charset.forName(
                                    HttpHeaderParser.parseCharset(
                                        response.headers,
                                        "utf-8"
                                    )
                                )
                            )
                            // Now you can use any deserializer to make sense of data
//                        val obj = JSONObject(res);
                            Log.d("getMember", "RES : $res")
                        } catch (e1: UnsupportedEncodingException) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (e2: JSONException) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }

                    }
                }
            )
            {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    val container = "Bearer " + preferences.getString("token", null)!!
                    headers["Authorization"] = container
                    headers["Content-Type"] = "application/json"
                    return headers
                }
                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }
    }
}