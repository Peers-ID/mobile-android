package com.android.id.peers.loans_unused.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.work.Data
import com.android.id.peers.util.callback.LoanApplicationCallback
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.connection.ApiConnections.Companion.API_HOSTNAME
import com.android.id.peers.util.connection.VolleyRequestSingleton
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

@Parcelize
@Entity(tableName = "loans")
data class Loan constructor (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    @ColumnInfo(name = "ao_id") var aoId : Int = 0,
    @ColumnInfo(name = "member_id") var memberId : Int = 0,
    @Ignore var memberName : String = "",
    @ColumnInfo(name = "member_handphone") var noHp : String = "",
    @ColumnInfo(name = "formula_id") var formulaId : Int = 0,
    @ColumnInfo(name = "total_disbursed") var totalDisbursed : Long = 0,
    @ColumnInfo(name = "cicilan_ke") var cicilanKe : Int = 0,
    @ColumnInfo(name = "cicilan_per_bln") var cicilanPerBulan : Long = 0,
    @ColumnInfo(name = "jumlah_loan") var numberOfLoan : Long = 0,
    @ColumnInfo(name = "tenor") var tenor : Int = 0,
    @Ignore var serviceFee : Long = 0,
    @Ignore var otherFees : ArrayList<Pair<String, Long>>
) : Parcelable {
    constructor() : this(0, 0, 0, "", "", 0, 0, 0, 0, 0, 0, 0, ArrayList<Pair<String, Long>>())

    companion object {
        fun getCanApprove(preferences: SharedPreferences, context: Context) {
            val koperasiId = preferences.getInt("koperasi_id", 0)
            val url = "${API_HOSTNAME}koperasi/approval/$koperasiId"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status == 200) {
                        val data = jsonObj.getJSONArray("data")
                        if (data.length() > 0) {
                            val canApprove = data.getJSONObject(0).getInt("ao_can_approved")
                            preferences.edit().putInt("ao_can_edit", canApprove).apply()
                        }
                    }
                    Log.d("getLoanApproval", strResp)
                },
                Response.ErrorListener { error ->
                    Log.e("getLoanApproval", error.toString())
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

        fun getLoan(preferences: SharedPreferences, context: Context, callback: LoanDisbursement, listType: Int) {
            val aoId = preferences.getInt("id", 0).toString()

            var url = "${API_HOSTNAME}loan?ao_id=$aoId&is_loan_approved=$listType"
            if (listType == 0) {
                url = "${API_HOSTNAME}loan?ao_id=$aoId"
            }

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getLoan", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val loanArray = ArrayList<Loan>()
                        val data = jsonObj.getJSONArray("data")
                        (0 until data.length()).forEach { i ->
                            val loanObj = data.getJSONObject(i)
                            val loan = Loan(otherFees = ArrayList())
                            loan.id = loanObj.getInt("id")
                            loan.memberId = loanObj.getInt("member_id")
                            loan.memberName = loanObj.getString("member_name")
                            Log.d("getLoan", loan.memberName)
                            //                        loan.noHp = loanObj.getString("member_handphone")
                            loan.aoId = preferences.getInt("id", 0)
                            loan.tenor = loanObj.getInt("tenor")
                            //                        loan.formulaId = loanObj.getInt("formula_id")
                            loan.totalDisbursed = loanObj.getLong("total_disbursed")
                            loan.cicilanPerBulan = loanObj.getLong("cicilan_per_bln")

                            //get collection based on loan id
                            Log.d("getLoan", "List Type : $listType")
                            if (listType == 1) {
                                Log.d("getLoan", "MASUK")
                                getCollection(context, preferences, callback, loan)
                            } else {
                                loanArray.add(loan)
                            }
                        }

                        if (listType != 1) {
                            callback.onSuccess(loanArray)
                        } else {
                            if (loanArray.size == 0) {
                                callback.onSuccess(loanArray)
                            }
                        }
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

        fun getCollection(context: Context, preferences: SharedPreferences, callback: LoanDisbursement, loan: Loan) {
            val url = "${API_HOSTNAME}collection?loan_id=${loan.id}&cicilan_jumlah=null"

            Log.d("getCollection", "URL : $url")

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getString("status")
                    Log.d("getCollection", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                    if (status.toInt() == 200) {
                        val loanArray = ArrayList<Loan>()
                        val data = jsonObj.getJSONArray("data")
                        (0 until data.length()).forEach { i ->
                            val loanObj = data.getJSONObject(i)
                            val loanCollection = Loan(otherFees = ArrayList())
                            loanCollection.id = loanObj.getInt("loan_id")
                            loanCollection.memberId = loan.memberId
                            loanCollection.memberName = loan.memberName
                            loanCollection.cicilanPerBulan = loan.cicilanPerBulan
                            loanCollection.cicilanKe = loanObj.getInt("cicilan_ke")
                            loanCollection.tenor = loan.tenor
                            //                        loan.noHp = loanObj.getString("member_handphone")
                            loanCollection.aoId = loan.aoId
                            //                        loan.formulaId = loanObj.getInt("formula_id")
                            loanCollection.totalDisbursed = loan.totalDisbursed
                            loanArray.add(loanCollection)
                        }

                        //get collection based on load id

                        callback.onSuccess(loanArray)
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

        fun getLoanApproval(preferences: SharedPreferences, context: Context, loanId: Int, loanApprovalStatus: Int) {
            val url = "${API_HOSTNAME}loan_approval/$loanId/$loanApprovalStatus"

            val jsonObjectRequest = object: JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response ->
                    val strResp = response.toString()
                    Log.d("getLoanApproval", strResp)
                },
                Response.ErrorListener { error ->
                    Log.e("getLoanApproval", error.toString())
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

        fun postLoan(loan: Loan, preferences: SharedPreferences, context: Context, loanApplicationCallback: LoanApplicationCallback? = null) {
            val url = "${API_HOSTNAME}loan"

            val params = HashMap<String, String>()

            Log.d("LoanConfirmation", loan.aoId.toString())
            Log.d("LoanConfirmation", loan.formulaId.toString())
            Log.d("LoanConfirmation", loan.totalDisbursed.toString())
            Log.d("LoanConfirmation", loan.cicilanPerBulan.toString())


            params["koperasi_id"] = preferences.getInt("koperasi_id", 0).toString()
            Log.d("postLoan", "koperasi id ${preferences.getInt("koperasi_id", 0)}")
            Log.d("postLoan", "ao id ${loan.aoId}")
            Log.d("postLoan", "formula id ${loan.formulaId}")
//        params["member_id"] = loan.memberId.toString()
            params["member_handphone"] = loan.noHp
//        params["ao_id"] = loan.aoId.toString()
            params["formula_id"] = loan.formulaId.toString()
            params["jumlah_loan"] = loan.numberOfLoan.toString()
            params["total_disbursed"] = loan.totalDisbursed.toString()
            params["tenor"] = loan.tenor.toString()
            params["cicilan_per_bln"] = loan.cicilanPerBulan.toString()

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
                    memberPreferences.edit().putString("no_hp", loan.noHp).apply()
                    val canApprove = preferences.getInt("ao_can_approve", 0)
                    if(canApprove == 1) {
                        getLoanApproval(preferences, context, loan.id, 3)
                    }
                    val jsonObj = JSONObject(strResp)
                    val status = jsonObj.getInt("status")
                    Log.d("postLoan", strResp)
                    if (status == 401) {
                        loanApplicationCallback?.onSuccess(false)
                        //callback loan application failed
                    } else {
                        loanApplicationCallback?.onSuccess(true)
                    }
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

        fun postCollection(collection: Collection, preferences: SharedPreferences, context: Context) {
            val url = "${API_HOSTNAME}collection"

            val params = HashMap<String, String>()
//        params["token"] = preferences.getString("token", null)!!

            params["koperasi_id"] = preferences.getInt("koperasi_id", 0).toString()
            params["member_id"] = collection.memberId.toString()
            params["ao_id"] = collection.aoId.toString()
            params["loan_id"] = collection.loanId.toString()
            params["cicilan_ke"] = collection.cicilanKe.toString()
            params["cicilan_jumlah"] = collection.cicilanJumlah.toString()
            params["pokok"] = collection.pokok.toString()
            params["sukarela"] = collection.sukarela.toString()

//            Log.d("postCollection", "koperasi_id : ${params["koperasi_id"]}")
//            Log.d("postCollection", "member_id : ${params["member_id"]}")
//            Log.d("postCollection", "ao_id : ${params["ao_id"]}")
//            Log.d("postCollection", "loan_id : ${params["loan_id"]}")
//            Log.d("postCollection", "cicilan_ke : ${params["cicilan_ke"]}")
//            Log.d("postCollection", "cicilan_jumlah : ${params["cicilan_jumlah"]}")
//            Log.d("postCollection", "pokok : ${params["pokok"]}")
//            Log.d("postCollection", "sukarela : ${params["sukarela"]}")

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
//                val jsonObj = JSONObject(strResp)
//                val loginStatus = jsonObj.getString("status")
                    Log.d("postCollection", strResp)
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

        fun saveLoans(configPreferences: SharedPreferences, result: List<Loan>, key: String) {
            Log.d("saveLoans", "key : $key")
            val json = Gson().toJson(result)
            configPreferences.edit().putString(key, json).apply()
        }

        @SuppressLint("RestrictedApi")
        fun putLoanOnDataBuilder(loan: Loan): Data.Builder {
            val data = Data.Builder()
            data.put("no_hp", loan.noHp)
            data.put("number_of_loan", loan.numberOfLoan)
            data.put("tenor", loan.tenor)
            data.put("formula_id", loan.formulaId)
            data.put("ao_id", loan.aoId)
            data.put("total_disbursed", loan.totalDisbursed)
            data.put("cicilan_per_bln", loan.cicilanPerBulan)
            data.put("service_fee", loan.serviceFee)
            return data
        }
    }
}