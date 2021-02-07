package com.android.id.peers.loans_unused.models

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.id.peers.util.callback.LoanFormulaCallback
import com.android.id.peers.util.connection.ApiConnections.Companion.API_HOSTNAME
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

class LoanFormulaConfig {
    var id: Int = 0
    var formulaName: String = ""
    var minLoanAmount: Int = 0
    var maxLoanAmount: Int = 0
    var kelipatan: Int = 0
    var minTenure: Int = 1
    var maxTenure: Int = 1
    var tenureCycle: String = "tahun"
    var serviceType: String = "fixed"
    var serviceAmount: Long = 0
    var serviceCycle: String = "tahun"

    companion object {
        fun saveLoanFormula(configPreferences: SharedPreferences, result: LoanFormulaConfig) {
            configPreferences.edit()
                .putInt("id", result.id)
                .putString("formula_name", result.formulaName)
                .putInt("min_loan_amount", result.minLoanAmount)
                .putInt("max_loan_amount", result.maxLoanAmount)
                .putInt("kelipatan", result.kelipatan)
                .putInt("min_tenure", result.minTenure)
                .putInt("max_tenure", result.maxTenure)
                .putString("tenure_cycle", result.tenureCycle)
                .putString("service_type", result.serviceType)
                .putLong("service_amount", result.serviceAmount)
                .putString("service_cycle", result.serviceCycle)
                .apply()
        }

        fun getLoanFormula(preferences: SharedPreferences, context: Context, callback: LoanFormulaCallback) {
            var url = "${API_HOSTNAME}loan/formula/"
            val id = preferences.getInt("koperasi_id", 0)
            url += id

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getLoanFormula", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val config = LoanFormulaConfig()
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            val firstObj = data.getJSONObject(0)
                            config.id = firstObj.getInt("id")
                            config.formulaName = firstObj.getString("formula_name")
                            config.minLoanAmount = firstObj.getInt("min_loan_amount")
                            config.maxLoanAmount = firstObj.getInt("max_loan_amount")
                            config.kelipatan = firstObj.getInt("kelipatan")
                            config.minTenure = firstObj.getInt("min_tenure")
                            config.maxTenure = firstObj.getInt("max_tenure")
                            config.tenureCycle = firstObj.getString("tenure_cycle")
                            config.serviceType = firstObj.getString("service_type")
                            config.serviceAmount = firstObj.getLong("service_amount")
                            config.serviceCycle = firstObj.getString("service_cycle")
                        }
                        getOtherFees(config.id, preferences, context, callback)
                        callback.onSuccess(config)
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
                            Log.d("getLoanFormula", "RES : $res")
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

        fun getOtherFees(id : Int, preferences: SharedPreferences, context: Context, callback: LoanFormulaCallback) {
            var url = "${API_HOSTNAME}loan/other_fee/$id"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getOtherFees", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val configArray = ArrayList<OtherFees>()
                        val config = OtherFees()
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            for (index in 0 until data.length()) {
                                val feeObj = data.getJSONObject(index)
                                config.id = feeObj.getInt("id")
                                config.formulaId = feeObj.getInt("formula_id")
                                config.serviceName = feeObj.getString("service_name")
                                config.serviceType = feeObj.getString("service_type")
                                config.serviceAmount = feeObj.getLong("service_amount")
                                config.serviceCycle = feeObj.getString("service_cycle")
                                configArray.add(config)
                            }
                        }
                        callback.onSuccess(configArray)
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
                            Log.d("getOtherFees", "RES : $res")
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