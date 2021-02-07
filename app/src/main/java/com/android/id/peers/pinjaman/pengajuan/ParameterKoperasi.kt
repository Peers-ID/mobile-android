package com.android.id.peers.pinjaman.pengajuan

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.id.peers.util.callback.ParameterCallback
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

class ParameterKoperasi {
    var hariPerBulan : Int = 0
    var idAngsuranSebagian : Int = 0 /* 1 = mengurangi pokok, 2 = mengurangi bunga*/
    var masaTenggang : Int = 0
    var typeDendaKeterlambatan : String = "Fix" /*Fix atau Persen*/
    var idDasarDenda : Int = 0 /*0 = tidak ada (Fix), 1 = angsuran (pokok + bunga), 2 = pokok pinjaman*/
    var typePelunasanDipercepat : String = "Fix" /*Fix atau Persen*/
    var idDasarPelunasan : Int = 0 /* 0 = tidak ada (Fix), 1 = total sisa pinjaman, 2 = total pokok pinjaman*/
    var idUrutanSimpanan : String = "" /*Pelunasan dengan simpanan tidak aktif, 1|2|3 = pokok|wajib|sukarela*/

    companion object {
        fun getParameterKoperasi(preferences: SharedPreferences, context: Context, callback: ParameterCallback) {
            val url = "${ApiConnections.API_HOSTNAME}parameter"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("getMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status == 200) {
                        val parameterKoperasi = ParameterKoperasi()
                        val dataArray = jsonObj.getJSONArray("data")
                        val data = dataArray.getJSONObject(0)

                        parameterKoperasi.hariPerBulan = data.getInt("hari_per_bulan")
                        parameterKoperasi.idAngsuranSebagian = data.getInt("id_angsuran_sebagian")
                        parameterKoperasi.masaTenggang = data.getInt("id_masa_tenggang")
                        parameterKoperasi.typeDendaKeterlambatan = data.getString("type_denda_keterlambatan")
                        parameterKoperasi.idDasarDenda = data.getInt("id_dasar_denda")
                        parameterKoperasi.typePelunasanDipercepat = data.getString("type_pelunasan_dipercepat")
                        parameterKoperasi.idDasarPelunasan = data.getInt("id_dasar_pelunasan")
                        parameterKoperasi.idUrutanSimpanan = data.getString("id_urutan_simpanan")

                        callback.onSuccess(parameterKoperasi)
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
                            e1.printStackTrace()
                        } catch (e2: JSONException) {
                            // returned data is not JSONObject?
                            e2.printStackTrace()
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
