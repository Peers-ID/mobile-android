package com.android.id.peers.util.connection

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.loans.models.LoanFormulaConfig
import com.android.id.peers.loans.models.OtherFees
import com.android.id.peers.members.models.Member
import com.android.id.peers.members.models.MemberAcquisitionConfig
import com.android.id.peers.util.callback.RepaymentCollection
import com.android.id.peers.util.callback.LoanDisbursement
import com.android.id.peers.util.callback.SplashScreen
import com.android.volley.AuthFailureError
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class ApiConnections {
    companion object {
        val REQUEST_TYPE_PLAIN = 0
        val REQUEST_TYPE_GET_CONFIG = 1
        val REQUEST_TYPE_GET_MEMBER = 2
        val REQUEST_TYPE_POST_MEMBER = 3
        val REQUEST_TYPE_CHECK_PHONE_NUMBER = 4
        val REQUEST_TYPE_GET_LOAN_FORMULA = 5
        val REQUEST_TYPE_GET_OTHER_FEES = 6
        val REQUEST_TYPE_GET_LOAN = 7
        val REQUEST_TYPE_POST_LOAN = 8
    }

    public fun authenticate(preferences: SharedPreferences, context: Context, requestType: Int, mParam: Any?, memberId: Int = 0) {
        val url = "http://dev-api.peers.id/api/v1/login"

        val networkConnectivity = NetworkConnectivity(context)
        if(networkConnectivity.isConnectedOverWifi()) {
            networkConnectivity.deleteCache()
        }

        val params = HashMap<String, String>()
        params["email"] = preferences.getString("email", null)!!
        params["password"] = preferences.getString("password", null)!!

        val parameters = JSONObject(params as Map<*, *>);
        val jsonObjectRequest = JsonObjectRequest(
            Method.POST, url, parameters,
            Response.Listener { response ->
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val loginStatus = jsonObj.getString("status")
                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {

                } else if (loginStatus.toInt() == 201) {
                    val dataJsonObj = jsonObj.getJSONObject("data")
                    val token = dataJsonObj.getString("token")
                    Log.d("authenticate", token)
                    preferences.edit().putString("token", token).apply()
                    when (requestType) {
                        REQUEST_TYPE_GET_CONFIG ->
                        {
                            val param = mParam as SplashScreen
                            getConfig(preferences, context, param)
                            getLoanFormula(preferences, context, param)
                        }
                        REQUEST_TYPE_GET_MEMBER -> getMember(preferences, context, mParam as RepaymentCollection, memberId!!)
                        REQUEST_TYPE_POST_MEMBER -> postMember(mParam as Member, preferences, context)
                        REQUEST_TYPE_GET_LOAN -> getLoan(preferences, context, mParam as LoanDisbursement)
                        REQUEST_TYPE_POST_LOAN -> postLoan(mParam as Loan, preferences, context)
                    }

//                    popUpSnack(view, "Login Success!")
                }
            },
            Response.ErrorListener { error ->
                Log.e("authenticate", error.toString())
            }
        )

        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }

    public fun getConfig(preferences: SharedPreferences, context: Context, callback: SplashScreen) {
        var url = "http://dev-api.peers.id/api/v1/member_config/"
//        val id = preferences.getInt("koperasi_id", 0)
        val id = 4
        url += id

        val jsonObjectRequest = object: JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val status = jsonObj.getString("status")
                Log.d("getConfig", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                if (status.toInt() == 200) {
                    val config = MemberAcquisitionConfig()
                    val data = jsonObj.getJSONArray("data")
                    val firstObj = data.getJSONObject(0)
                    val idKoperasi = firstObj.getInt("koperasi_id")
                    config.jenisIdentitas = firstObj.getInt("jenis_identitas")
                    config.noIdentitas = firstObj.getInt("no_identitas")
                    config.namaLengkap = firstObj.getInt("nama_lengkap")
//                    config.noHp = firstObj.getInt("no_hp")
                    config.tanggalLahir = firstObj.getInt("tanggal_lahir")
//                    config.tempatLahir = firstObj.getInt("tempat_lahir")
                    config.jenisKelamin = firstObj.getInt("jenis_kelamin")
                    config.namaGadisIbu = firstObj.getInt("nama_gadis_ibu")
                    config.statusPerkawinan = firstObj.getInt("status_perkawinan")
                    config.pendidikanTerakhir = firstObj.getInt("pendidikan_terakhir")
                    config.alamatKtpJalan = firstObj.getInt("alamat_ktp_jalan")
                    config.alamatKtpNo = firstObj.getInt("alamat_ktp_nomer")
                    config.alamatKtpRt = firstObj.getInt("alamat_ktp_rt")
                    config.alamatKtpRw = firstObj.getInt("alamat_ktp_rw")
                    config.alamatKtpKelurahan = firstObj.getInt("alamat_ktp_kelurahan")
                    config.alamatKtpKecamatan = firstObj.getInt("alamat_ktp_kecamatan")
                    config.alamatKtpKota = firstObj.getInt("alamat_ktp_kota")
                    config.alamatKtpProvinsi = firstObj.getInt("alamat_ktp_provinsi")
                    config.alamatKtpStatusTempatTinggal = firstObj.getInt("alamat_ktp_status_tempat_tinggal")
                    config.alamatKtpLamaTinggal = firstObj.getInt("alamat_ktp_lama_tinggal")
                    config.domisiliSesuaiKtp = firstObj.getInt("domisili_sesuai_ktp")
                    config.alamatDomisiliJalan = firstObj.getInt("alamat_domisili_jalan")
                    config.alamatDomisiliNo = firstObj.getInt("alamat_domisili_nomer")
                    config.alamatDomisiliRt = firstObj.getInt("alamat_domisili_rt")
                    config.alamatDomisiliRw = firstObj.getInt("alamat_domisili_rw")
                    config.alamatDomisiliKelurahan = firstObj.getInt("alamat_domisili_kelurahan")
                    config.alamatDomisiliKecamatan = firstObj.getInt("alamat_domisili_kecamatan")
//                    config.alamatDomisiliKotaProvinsi = firstObj.getInt("alamat_domisili_kota_provinsi")
                    config.alamatDomisiliKota = firstObj.getInt("alamat_domisili_kota")
                    config.alamatDomisiliProvinsi = firstObj.getInt("alamat_domisili_provinsi")
                    config.alamatDomisiliStatusTempatTinggal = firstObj.getInt("alamat_domisili_status_tempat_tinggal")
                    config.alamatDomisiliLamaTempatTinggal = firstObj.getInt("alamat_domisili_lama_tempat_tinggal")
                    config.memilikiNpwp = firstObj.getInt("memiliki_npwp")
                    config.nomerNpwp = firstObj.getInt("nomer_npwp")
                    config.pekerjaUsaha = firstObj.getInt("pekerja_usaha")
                    config.bidangPekerja = firstObj.getInt("bidang_pekerja")
                    config.posisiJabatan = firstObj.getInt("posisi_jabatan")
                    config.namaPerusahaan = firstObj.getInt("nama_perusahaan")
                    config.lamaBekerja = firstObj.getInt("lama_bekerja")
                    config.penghasilanOmset = firstObj.getInt("penghasilan_omset")
                    config.alamatKantorJalan = firstObj.getInt("alamat_kantor_jalan")
                    config.alamatKantorNo = firstObj.getInt("alamat_kantor_nomer")
                    config.alamatKantorRt = firstObj.getInt("alamat_kantor_rt")
                    config.alamatKantorRw = firstObj.getInt("alamat_kantor_rw")
                    config.alamatKantorKelurahan = firstObj.getInt("alamat_kantor_kelurahan")
                    config.alamatKantorKecamatan = firstObj.getInt("alamat_kantor_kecamatan")
//                    config.alamatKantorKotaProvinsi = firstObj.getInt("alamat_kantor_kota_provinsi")
                    config.alamatKantorKota = firstObj.getInt("alamat_kantor_kota")
                    config.alamatKantorProvinsi = firstObj.getInt("alamat_kantor_provinsi")
                    config.namaEmergency = firstObj.getInt("nama")
                    config.noHpEmergency = firstObj.getInt("no_hp")
                    config.hubungan = firstObj.getInt("hubungan")
                    callback.onSuccess(config)

                    Log.d("getConfig", "$idKoperasi")
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
                        Log.d("getConfig", "RES : $res")
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

    private fun getLoanFormula(preferences: SharedPreferences, context: Context, callback: SplashScreen) {
        var url = "http://dev-api.peers.id/api/v1/loan/formula/"
//        val id = preferences.getInt("koperasi_id", 0) TODO: uncomment this
        val id = 20 //TODO: remove this
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

    private fun getOtherFees(id : Int, preferences: SharedPreferences, context: Context, callback: SplashScreen) {
        var url = "http://dev-api.peers.id/api/v1/loan/other_fee/"
        url += id

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

    private fun getLoan(preferences: SharedPreferences, context: Context, callback: LoanDisbursement) {
        val url = "http://dev-api.peers.id/api/v1/loan/"
//        val id = preferences.getInt("koperasi_id", 0) TODO: uncomment this
//        val id = 20 //TODO: remove this
//        url += id

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
                    for (index in 0 until data.length()) {
                        val loanObj = data.getJSONObject(index)
                        val loan = Loan(otherFees = ArrayList())
                        loan.memberId = loanObj.getInt("member_id")
                        loan.memberName = loanObj.getString("member_name")
                        Log.d("getLoan", loan.memberName)
//                        loan.noHp = loanObj.getString("member_handphone")
//                        loan.aoId = loanObj.getInt("ao_id")
//                        loan.formulaId = loanObj.getInt("formula_id")
                        loan.totalDisbursed = loanObj.getLong("total_disbursed")
                        loan.cicilanPerBulan = loanObj.getLong("cicilan_per_bln")
                        loanArray.add(loan)
                    }
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

    private fun getMember(preferences: SharedPreferences, context: Context, callback: RepaymentCollection, memberId: Int) {
//        val url = "http://dev-api.peers.id/api/v1/member/$memberId"
        val url = "http://dev-api.peers.id/api/v1/member/6" //TODO: uncomment this

        val jsonObjectRequest = object: JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val status = jsonObj.getString("status")
                Log.d("getMember", strResp)
//                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
//                } else if (loginStatus.toInt() == 201) {
//                }
                if (status.toInt() == 200) {
                    val member = Member()
                    val data = jsonObj.getJSONArray("data")
                    val memberJsonObj = data.getJSONObject(0)
                    member.setMemberJenisIdentitas(memberJsonObj.getString("jenis_identitas"))
                    member.noIdentitas = memberJsonObj.getString("no_identitas")
                    member.namaLengkap = memberJsonObj.getString("nama_lengkap")
                    member.noIdentitas = memberJsonObj.getString("no_identitas")
                    member.noHp = memberJsonObj.getString("member_handphone")
                    member.tanggalLahir = memberJsonObj.getString("tanggal_lahir")
                    member.tempatLahir = memberJsonObj.getString("tempat_lahir")
                    Log.d("getMember", "Jenis Kelamin : ${memberJsonObj.getString("jenis_kelamin")}")
                    member.setMemberJenisKelamin(memberJsonObj.getString("jenis_kelamin"))
                    member.namaGadisIbuKandung = memberJsonObj.getString("nama_gadis_ibu")
                    member.setMemberStatusPerkawinan(memberJsonObj.getString("status_perkawinan"))
                    member.setMemberPendidikanTerakhir(memberJsonObj.getString("pendidikan_terakhir"))

                    member.jalanSesuaiKtp = memberJsonObj.getString("alamat_ktp_jalan")
                    Log.d("getMember", member.jalanSesuaiKtp)
                    member.nomorSesuaiKtp = memberJsonObj.getString("alamat_ktp_nomer")
                    member.rtSesuaiKtp = memberJsonObj.getString("alamat_ktp_rt")
                    member.rwSesuaiKtp = memberJsonObj.getString("alamat_ktp_rw")
                    member.kelurahanSesuaiKtp = memberJsonObj.getString("alamat_ktp_kelurahan")
                    member.kecamatanSesuaiKtp = memberJsonObj.getString("alamat_ktp_kecamatan")
                    member.kotaSesuaiKtp = memberJsonObj.getString("alamat_ktp_kota")
                    member.provinsiSesuaiKtp = memberJsonObj.getString("alamat_ktp_provinsi")
                    member.setStatusTempatTinggalSesuaiKtp(memberJsonObj.getString("alamat_ktp_status_tempat_tinggal"))
                    val lamaTinggalSesuaiKtp = memberJsonObj.getInt("alamat_ktp_lama_tinggal")
                    member.lamaTahunTinggalSesuaiKtp = lamaTinggalSesuaiKtp / 12
                    member.lamaTahunTinggalSesuaiKtp = lamaTinggalSesuaiKtp % 12

                    member.jalanDomisili = memberJsonObj.getString("alamat_domisili_jalan")
                    member.nomorDomisili = memberJsonObj.getString("alamat_domisili_nomer")
                    member.rtDomisili = memberJsonObj.getString("alamat_domisili_rt")
                    member.rwDomisili = memberJsonObj.getString("alamat_domisili_rw")
                    member.kelurahanDomisili = memberJsonObj.getString("alamat_domisili_kelurahan")
                    member.kecamatanDomisili = memberJsonObj.getString("alamat_domisili_kecamatan")
                    member.kotaDomisili = memberJsonObj.getString("alamat_domisili_kota")
                    member.provinsiDomisili = memberJsonObj.getString("alamat_domisili_provinsi")
                    member.setStatusTempatTinggalDomisili(memberJsonObj.getString("alamat_domisili_status_tempat_tinggal"))
                    val lamaTinggalDomisili = memberJsonObj.getInt("alamat_domisili_lama_tempat_tinggal")
                    member.lamaTahunTinggalDomisili = lamaTinggalDomisili / 12
                    member.lamaTahunTinggalDomisili = lamaTinggalDomisili % 12

                    member.memilikiNpwp = if(memberJsonObj.getInt("memiliki_npwp") == 0) 1 else 0
                    member.nomorNpwp = memberJsonObj.getString("nomer_npwp")
                    member.setPekerjaan(memberJsonObj.getString("pekerja_usaha"))
                    member.bidangPekerjaan = memberJsonObj.getString("bidang_pekerja")
                    member.posisiPekerjaan = memberJsonObj.getString("posisi_jabatan")
                    member.namaPerusahaan = memberJsonObj.getString("nama_perusahaan")

                    val lamaBekerja = memberJsonObj.getInt("lama_bekerja")
                    member.lamaTahunBekerja = lamaBekerja / 12
                    member.lamaBulanBekerja = lamaBekerja % 12
                    member.penghasilan = memberJsonObj.getLong("penghasilan_omset")
                    member.jalanKantor = memberJsonObj.getString("alamat_kantor_jalan")
                    member.nomorKantor = memberJsonObj.getString("alamat_kantor_nomer")
                    member.rtKantor = memberJsonObj.getString("alamat_kantor_rt")
                    member.rwKantor = memberJsonObj.getString("alamat_kantor_rw")
                    member.kelurahanKantor = memberJsonObj.getString("alamat_kantor_kelurahan")
                    member.kecamatanKantor = memberJsonObj.getString("alamat_kantor_kecamatan")
                    member.kotaKantor = memberJsonObj.getString("alamat_kantor_kota")
                    member.provinsiKantor = memberJsonObj.getString("alamat_kantor_provinsi")

                    member.namaEmergency = memberJsonObj.getString("nama")
                    member.noHpEmergency = memberJsonObj.getString("no_hp")
                    member.setHubunganEmergency(memberJsonObj.getString("hubungan"))

                    callback.onSuccess(member)
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

    private fun postMember(member: Member, preferences: SharedPreferences, context: Context) {
        val url = "http://dev-api.peers.id/api/v1/member"

        val params = HashMap<String, String>()
//        params["token"] = preferences.getString("token", null)!!

        params["jenis_identitas"] = member.memberJenisIdentitasString()
        params["no_identitas"] = member.noIdentitas
        params["nama_lengkap"] = member.namaLengkap
        params["member_handphone"] = member.noHp
        params["tanggal_lahir"] = member.tanggalLahir
        params["tempat_lahir"] = member.tempatLahir
        params["jenis_kelamin"] = member.jenisKelaminString()
        params["nama_gadis_ibu"] = member.namaGadisIbuKandung
        params["status_perkawinan"] = member.statusPerkawinanString()
        params["pendidikan_terakhir"] = member.pendidikanTerakhirString()

        params["alamat_ktp_jalan"] = member.jalanSesuaiKtp
        params["alamat_ktp_nomer"] = member.nomorSesuaiKtp
        params["alamat_ktp_rt"] = member.rtSesuaiKtp
        params["alamat_ktp_rw"] = member.rwSesuaiKtp
        params["alamat_ktp_kelurahan"] = member.kelurahanSesuaiKtp
        params["alamat_ktp_kecamatan"] = member.kecamatanSesuaiKtp //tidak ada di API
//        params["alamat_ktp_kota_provinsi"] = member.kotaSesuaiKtp //tidak ada di API
        params["alamat_ktp_kota"] = member.kotaSesuaiKtp
        params["alamat_ktp_provinsi"] = member.provinsiSesuaiKtp
        params["alamat_ktp_status_tempat_tinggal"] = member.statusTempatTinggalSesuaiKtpString()
        params["alamat_ktp_lama_tinggal"] = ((member.lamaTahunTinggalSesuaiKtp - 1) * 12 + (member.lamaBulanTinggalSesuaiKtp - 1)).toString()
        params["domisili_sesuai_ktp"] = member.domisiliSesuaiKtp.toString()
        params["alamat_domisili_jalan"] = member.jalanDomisili
        params["alamat_domisili_nomer"] = member.nomorDomisili
        params["alamat_domisili_rt"] = member.rtDomisili
        params["alamat_domisili_rw"] = member.rwDomisili
        params["alamat_domisili_kelurahan"] = member.kelurahanDomisili
        params["alamat_domisili_kecamatan"] = member.kecamatanDomisili
//        params["alamat_domisili_kota_provinsi"] = member.kotaDomisili
        params["alamat_domisili_kota"] = member.kotaDomisili
        params["alamat_domisili_provinsi"] = member.provinsiDomisili
        params["alamat_domisili_status_tempat_tinggal"] = member.statusTempatTinggalDomisiliString()
        params["alamat_domisili_lama_tempat_tinggal"] = ((member.lamaTahunTinggalDomisili - 1) * 12 + (member.lamaBulanTinggalDomisili - 1)).toString()

        params["memiliki_npwp"] = if (member.memilikiNpwp == 0) "1" else "0"
        params["nomer_npwp"] = member.nomorNpwp
        params["pekerja_usaha"] = member.pekerjaanString()
        params["bidang_pekerja"] = member.bidangPekerjaan
        params["posisi_jabatan"] = member.posisiPekerjaan
        params["nama_perusahaan"] = member.namaPerusahaan
        params["lama_bekerja"] = ((member.lamaTahunBekerja - 1) * 12 + (member.lamaBulanBekerja - 1)).toString()
        params["penghasilan_omset"] = member.penghasilan.toString()
        params["alamat_kantor_jalan"] = member.jalanKantor
        params["alamat_kantor_nomer"] = member.nomorKantor
        params["alamat_kantor_rt"] = member.rtKantor
        params["alamat_kantor_rw"] = member.rwKantor
        params["alamat_kantor_kelurahan"] = member.kelurahanKantor
        params["alamat_kantor_kecamatan"] = member.kecamatanKantor
//        params["alamat_kantor_kota_provinsi"] = member.kotaKantor
        params["alamat_kantor_kota"] = member.kotaKantor
        params["alamat_kantor_provinsi"] = member.provinsiKantor

        params["nama"] = member.namaEmergency
        params["no_hp"] = member.noHpEmergency
        params["hubungan"] = member.hubunganEmergencyString()

        val parameters = JSONObject(params as Map<*, *>)
        val jsonObjectRequest = object: JsonObjectRequest(
            Method.POST, url, parameters,
            Response.Listener { response ->
                val strResp = response.toString()
                val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
                memberPreferences.edit().putString("no_hp", member.noHp).apply()
//                val jsonObj = JSONObject(strResp)
//                val loginStatus = jsonObj.getString("status")
                Log.d("postMember", strResp)
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

    private fun postLoan(loan: Loan, preferences: SharedPreferences, context: Context) {
        val url = "http://dev-api.peers.id/api/v1/loan"

        val params = HashMap<String, String>()
//        params["token"] = preferences.getString("token", null)!!

        Log.d("LoanConfirmation", loan.aoId.toString())
        Log.d("LoanConfirmation", loan.formulaId.toString())
        Log.d("LoanConfirmation", loan.totalDisbursed.toString())
        Log.d("LoanConfirmation", loan.cicilanPerBulan.toString())

        params["member_id"] = "2"
        params["member_handphone"] = loan.noHp
        params["ao_id"] = loan.aoId.toString()
        params["formula_id"] = loan.formulaId.toString()
        params["total_disbursed"] = loan.totalDisbursed.toString()
        params["cicilan_per_bln"] = loan.cicilanPerBulan.toString()
        params["member_photo_url"] = ""
        params["is_loan_approved"] = "0"

        val parameters = JSONObject(params as Map<*, *>)
        val jsonObjectRequest = object: JsonObjectRequest(
            Method.POST, url, parameters,
            Response.Listener { response ->
                val strResp = response.toString()
                val memberPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE)
                memberPreferences.edit().putString("no_hp", loan.noHp).apply()
//                val jsonObj = JSONObject(strResp)
//                val loginStatus = jsonObj.getString("status")
                Log.d("postLoan", strResp)
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