package com.android.id.peers.pinjaman.pengajuan

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import com.android.id.peers.util.callback.ProductCallback
import com.android.id.peers.util.connection.ApiConnections
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

@Parcelize
data class Product(
    var id: Int = 0,
    var namaProduct: String = "",
    var tenor: Int = 0,
    var satuanTenor: String = "bulan",
    var bunga: Double = 0.0,
    var tenorBunga: String = "bulan",
    var admin: Double = 0.0,
    var typeAdmin: String = "Fix",
    var provisi: Double = 0.0,
    var typeProvisi: String = "Fix",
    var asuransi: Double = 0.0,
    var typeAsuransi: String = "Fix",
    var jpk: Double = 0.0,
    var typeJpk: String = "Fix",
    var simpananPokok: Double = 0.0,
    var typeSimpananPokok: String = "Fix",
    var simpananWajib: Long = 0,
    var dendaKeterlambatan: Double = 0.0,
    var typeDendaKeterlambatan: String = "Fix",
    var pelunasanDipercepat: Double = 0.0,
    var typePelunasanDipercepat: String = "Persen",
    var status: String = "active"
) : Parcelable {

    override fun toString(): String {
        return namaProduct
    }

    companion object {
        fun getActiveProducts(preferences: SharedPreferences, context: Context, callback: ProductCallback) {
            val url = "${ApiConnections.API_HOSTNAME}product/active"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("getActiveProducts", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status == 200) {
                        val products = ArrayList<Product>()
                        val data = jsonObj.getJSONArray("data")
                        Log.d("getActiveProducts", "JUMLAH PRODUK : ${data.length()}")
                        for (idx in 0 until data.length()) {
                            val productJson = data.getJSONObject(idx)
                            val product = Product()
                            product.id = productJson.getInt("id")
                            product.namaProduct = productJson.getString("nama_produk")
                            product.tenor = productJson.getInt("tenor")
                            product.satuanTenor = productJson.getString("satuan_tenor")
                            product.bunga = productJson.getString("bunga").replace(",", ".").toDouble()
                            product.tenorBunga = productJson.getString("tenor_bunga")
                            product.admin = productJson.getDouble("admin")
                            product.typeAdmin = productJson.getString("type_admin")
                            product.provisi = productJson.getString("provisi").replace(",", ".").toDouble()
                            product.typeProvisi = productJson.getString("type_provisi")

                            product.asuransi = if(!productJson.isNull("asuransi")) productJson.getString("asuransi").replace(",", ".").toDouble() else 0.0
                            product.typeAsuransi = if(!productJson.isNull("type_asuransi")) productJson.getString("type_asuransi") else ""
                            product.jpk = if(!productJson.isNull("dana_jpk")) productJson.getString("dana_jpk").replace(",", ".").toDouble() else 0.0
                            product.typeJpk = if(!productJson.isNull("type_dana_jpk")) productJson.getString("type_dana_jpk") else ""

                            product.simpananPokok = productJson.getString("simpanan_pokok").replace(",", ".").toDouble()
                            product.typeSimpananPokok = productJson.getString("type_simpanan_pokok")
                            product.simpananWajib = productJson.getLong("simpanan_wajib")
                            product.dendaKeterlambatan = productJson.getString("denda_keterlambatan").replace(",", ".").toDouble()
                            product.typeDendaKeterlambatan = productJson.getString("type_denda_keterlambatan")
                            product.pelunasanDipercepat = productJson.getString("pelunasan_dipercepat").replace(",", ".").toDouble()
                            product.typePelunasanDipercepat = productJson.getString("type_pelunasan_dipercepat")

                            products.add(product)
                        }
                        callback.onSuccess(products)
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
                            Log.d("ActiveProduct", "RES : $res")
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