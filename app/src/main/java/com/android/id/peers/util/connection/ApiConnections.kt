package com.android.id.peers.util.connection

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.android.id.peers.BuildConfig
import com.android.id.peers.loans_unused.models.Loan
import com.android.id.peers.loans_unused.models.Loan.Companion.getLoan
import com.android.id.peers.loans_unused.models.Loan.Companion.postCollection
import com.android.id.peers.loans_unused.models.Loan.Companion.postLoan
import com.android.id.peers.loans_unused.models.LoanFormulaConfig.Companion.getLoanFormula
import com.android.id.peers.loans_unused.models.Collection
import com.android.id.peers.members.models.*
import com.android.id.peers.members.models.Member.Companion.getConfig
import com.android.id.peers.members.models.Member.Companion.getMember
import com.android.id.peers.members.models.Member.Companion.getMemberByNik
import com.android.id.peers.members.models.Member.Companion.postMember
import com.android.id.peers.members.models.Member.Companion.postMemberPicture
import com.android.id.peers.members.models.Member.Companion.postPicture
import com.android.id.peers.members.models.Member.Companion.putMember
import com.android.id.peers.pembayaran.Cicilan
import com.android.id.peers.pembayaran.Cicilan.Companion.getDetailCicilan
import com.android.id.peers.pembayaran.Cicilan.Companion.postPembayaranCicilan
import com.android.id.peers.pinjaman.pengajuan.ParameterKoperasi.Companion.getParameterKoperasi
import com.android.id.peers.pinjaman.pengajuan.Pinjaman
import com.android.id.peers.pinjaman.pengajuan.Pinjaman.Companion.getDetailPencairan
import com.android.id.peers.pinjaman.pengajuan.Pinjaman.Companion.getPembayaranCicilan
import com.android.id.peers.pinjaman.pengajuan.Pinjaman.Companion.getPencairanPinjaman
import com.android.id.peers.pinjaman.pengajuan.Pinjaman.Companion.getPinjamanMemberStatus
import com.android.id.peers.pinjaman.pengajuan.Pinjaman.Companion.putStatusPencairan
import com.android.id.peers.pinjaman.pengajuan.Product.Companion.getActiveProducts
import com.android.id.peers.simpanan.Simpanan
import com.android.id.peers.util.callback.*
import com.android.id.peers.util.database.OfflineViewModel
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import com.android.id.peers.util.callback.MemberCallback
import com.android.id.peers.util.repository.ApiRepository
import com.android.id.peers.util.repository.MemberRepository
import com.android.id.peers.util.response.ApiResponse
import retrofit2.Call
import retrofit2.Callback


class ApiConnections {
    companion object {
        const val REQUEST_TYPE_PLAIN = 0
        const val REQUEST_TYPE_GET_CONFIG = 1
        private const val REQUEST_TYPE_GET_MEMBER = 2
        const val REQUEST_TYPE_POST_MEMBER = 3
        const val REQUEST_TYPE_CHECK_PHONE_NUMBER = 4
        const val REQUEST_TYPE_GET_LOAN_FORMULA = 5
        const val REQUEST_TYPE_GET_OTHER_FEES = 6
        const val REQUEST_TYPE_GET_LOAN = 7
        const val REQUEST_TYPE_POST_LOAN = 8
        const val REQUEST_TYPE_GET_MEMBER_BY_PHONE = 9
        const val REQUEST_TYPE_POST_PICTURE = 10
        const val REQUEST_TYPE_POST_COLLECTION = 11
        const val REQUEST_TYPE_POST_CITCALL = 12
        const val REQUEST_TYPE_GET_CHECK_MEMBER_BY_NIK = 13
        const val REQUEST_TYPE_GET_ACTIVE_PRODUCTS = 14
        const val REQUEST_TYPE_GET_PARAMETER_KOPERASI = 15
        const val REQUEST_TYPE_GET_PINJAMAN_MEMBER_STATUS = 16
        const val REQUEST_TYPE_POST_MEMBER_PICTURE = 17
        const val REQUEST_TYPE_POST_MEMBER_AND_LOAN = 18
        const val REQUEST_TYPE_PUT_MEMBER_AND_LOAN = 19
        const val REQUEST_TYPE_GET_PENCAIRAN_PINJAMAN = 20
        const val REQUEST_TYPE_GET_DETAIL_PENCAIRAN = 21
        const val REQUEST_TYPE_PUT_STATUS_PENCAIRAN = 22
        const val REQUEST_TYPE_GET_PEMBAYARAN_CICILAN = 23
        const val REQUEST_TYPE_GET_DETAIL_CICILAN = 24
        const val REQUEST_TYPE_POST_PEMBAYARAN_CICILAN = 25
        const val REQUEST_TYPE_GET_SIMPANAN = 26
        const val REQUEST_TYPE_GET_REMBUG = 27
        const val REQUEST_TYPE_GET_KELOMPOK = 28
        const val API_HOSTNAME = BuildConfig.BASE_URL

        fun authenticate(preferences: SharedPreferences, context: Context, requestType: Int, mParam: Any? = null,
                                memberId: Int = 0, loanId: Int = 0, memberPhone: String = "", listType: Int = 0, fileName: String = "", loan: Loan? = null,
                                nik: String = "", pinjaman: Pinjaman? = null, member: Member? = null, imageData: ByteArray? = null, angsuran: Int = 0, cicilan: Cicilan? = null,rembugId: Int = 0) {
            val url = "${API_HOSTNAME}login"

            if (NetworkConnectivity.isConnectedOverWifi(context)) {
                NetworkConnectivity.deleteCache(context)
            }

            val params = HashMap<String, String>()
            params["email"] = preferences.getString("email", null)!!
            params["password"] = preferences.getString("password", null)!!

            val parameters = JSONObject(params as Map<*, *>);
            val jsonObjectRequest = JsonObjectRequest(
                Method.POST, url, parameters,
                { response ->
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
                            REQUEST_TYPE_GET_CONFIG -> {
                                val param = mParam as SplashScreen
                                getConfig(preferences, context, param)
//                                getCanApprove(preferences, context)
//                                getLoanFormula(preferences, context, param)
                            }
                            REQUEST_TYPE_GET_LOAN_FORMULA -> {
                                val param = mParam as LoanFormulaCallback
                                getLoanFormula(preferences, context, param)
                            }
                            REQUEST_TYPE_GET_MEMBER -> getMember(
                                preferences,
                                context,
                                false,
                                mParam as MembersCallback
                            )
//                            REQUEST_TYPE_GET_MEMBER_BY_PHONE -> getMemberByPhone(preferences, context, mParam as RepaymentCollection1, memberPhone)
                            REQUEST_TYPE_POST_MEMBER -> postMember(
                                member!!,
                                preferences,
                                context,
                                mParam as PinjamanResponseCallback,
                                pinjaman!!
                            )
//                            REQUEST_TYPE_PUT_MEMBER -> putMember(member!!, preferences, context, mParam as PinjamanResponseCallback, pinjaman!!)
                            REQUEST_TYPE_GET_LOAN -> getLoan(
                                preferences,
                                context,
                                mParam as LoanDisbursement,
                                listType
                            )
                            REQUEST_TYPE_POST_LOAN -> postLoan(
                                loan!!,
                                preferences,
                                context,
                                mParam as LoanApplicationCallback
                            )
                            REQUEST_TYPE_POST_PICTURE -> {
//                                getLoanApproval(preferences, context, loanId, 1)
                                postPicture(imageData!!, preferences, context, memberId, fileName)
                            }
                            REQUEST_TYPE_POST_MEMBER_AND_LOAN -> postMember(
                                member!!,
                                preferences,
                                context,
                                mParam as PinjamanResponseCallback,
                                pinjaman!!
                            )
                            REQUEST_TYPE_PUT_MEMBER_AND_LOAN -> putMember(
                                member!!,
                                preferences,
                                context,
                                mParam as PinjamanResponseCallback,
                                pinjaman!!
                            )
                            REQUEST_TYPE_POST_MEMBER_PICTURE -> {
                                postMemberPicture(
                                    imageData!!,
                                    preferences,
                                    context,
                                    fileName,
                                    mParam as PostPictureCallback
                                )
                            }
                            REQUEST_TYPE_POST_COLLECTION -> postCollection(
                                mParam as Collection,
                                preferences,
                                context
                            )
                            REQUEST_TYPE_POST_CITCALL -> postCitcall(
                                memberPhone,
                                preferences,
                                context,
                                mParam as CitcallCallback
                            )
                            REQUEST_TYPE_GET_CHECK_MEMBER_BY_NIK -> getMemberByNik(
                                preferences,
                                context,
                                mParam as MemberCallback,
                                nik
                            )
                            REQUEST_TYPE_GET_ACTIVE_PRODUCTS -> getActiveProducts(
                                preferences,
                                context,
                                mParam as ProductCallback
                            )
                            REQUEST_TYPE_GET_PARAMETER_KOPERASI -> getParameterKoperasi(
                                preferences,
                                context,
                                mParam as ParameterCallback
                            )
                            REQUEST_TYPE_GET_PINJAMAN_MEMBER_STATUS -> getPinjamanMemberStatus(
                                preferences,
                                context,
                                mParam as StatusPinjamanCallback
                            )
                            REQUEST_TYPE_GET_PENCAIRAN_PINJAMAN -> getPencairanPinjaman(
                                preferences,
                                context,
                                mParam as StatusPinjamanCallback
                            )
                            REQUEST_TYPE_GET_DETAIL_PENCAIRAN -> getDetailPencairan(
                                preferences,
                                context,
                                mParam as PencairanCallback,
                                loanId
                            )
                            REQUEST_TYPE_PUT_STATUS_PENCAIRAN -> putStatusPencairan(
                                preferences,
                                context,
                                memberId,
                                loanId
                            )
                            REQUEST_TYPE_GET_PEMBAYARAN_CICILAN -> getPembayaranCicilan(
                                preferences,
                                context,
                                mParam as StatusPinjamanCallback
                            )
                            REQUEST_TYPE_GET_DETAIL_CICILAN -> getDetailCicilan(
                                preferences,
                                context,
                                mParam as CicilanCallback,
                                memberId,
                                angsuran
                            )
                            REQUEST_TYPE_POST_PEMBAYARAN_CICILAN -> postPembayaranCicilan(
                                preferences,
                                context,
                                cicilan!!
                            )
                            REQUEST_TYPE_GET_SIMPANAN -> getMember(
                                preferences,
                                context,
                                true,
                                simpananItemCallback = mParam as SimpananItemCallback
                            )

                            REQUEST_TYPE_GET_REMBUG -> {
                                val token = "Bearer " + preferences.getString("token", null)!!

                                val apiResitory = ApiRepository.create()

                                apiResitory.getRembug(token).enqueue(object : Callback<ApiResponse> {
                                    override fun onResponse(
                                        call: Call<ApiResponse>,
                                        response: retrofit2.Response<ApiResponse>
                                    ) {
                                        val res = response.body()

                                        res.let {
                                            val status = it!!.status
                                            Log.d("getRembug", it.toString())

                                            if (status == 200) {
                                                var callback = mParam as ApiCallback
                                                callback.onSuccess(it)
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                        Log.e("getRembug", t.message.toString())
                                    }

                                })
                            }

                            REQUEST_TYPE_GET_KELOMPOK -> {
                                val token = "Bearer " + preferences.getString("token", null)!!

                                val apiResitory = ApiRepository.create()

                                apiResitory.getKelompok(token,rembugId).enqueue(object : Callback<ApiResponse> {
                                    override fun onResponse(
                                        call: Call<ApiResponse>,
                                        response: retrofit2.Response<ApiResponse>
                                    ) {
                                        val res = response.body()

                                        res.let {
                                            val status = it!!.status
                                            Log.d("getKelompok", it.toString())

                                            if (status == 200) {
                                                var callback = mParam as ApiCallback
                                                callback.onSuccess(it)
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                        Log.e("getKelompok", t.message.toString())
                                    }

                                })
                            }
                        }

//                    popUpSnack(view, "Login Success!")
                    }
                },
                { error ->
                    Log.e("authenticate", error.toString())
                }
            )

            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        suspend fun getWilayah(context: Context, owner: ViewModelStoreOwner, callback: WilayahCallback) {
            GlobalScope.async(Dispatchers.IO) {
                var countDesa = 0
                val strResp = URL("http://dashboard.peers.id/assets/indonesia-region.min.json").readText()
                val jsonArray = JSONArray(strResp)
                val provinceList = ArrayList<Province>()
                val kabupatenList = ArrayList<Kabupaten>()
                val kecamatanList = ArrayList<Kecamatan>()
                val desaList = ArrayList<Desa>()
                if (jsonArray.length() > 0) {
                    for (indexProvince in 0 until jsonArray.length()) {
                        val provinceJson = jsonArray.getJSONObject(indexProvince)
                        val province = Province()
                        province.id = provinceJson.getString("id")
                        province.name = provinceJson.getString("name")
                        provinceList.add(province)
                        val kabupatenArray = provinceJson.getJSONArray("regencies")
                        for (indexKabupaten in 0 until kabupatenArray.length()) {
                            val kabupatenJson = kabupatenArray.getJSONObject(indexKabupaten)
                            val kabupaten = Kabupaten()
                            kabupaten.id = kabupatenJson.getString("id")
                            kabupaten.provinceId = kabupatenJson.getString("province_id")
                            kabupaten.name = kabupatenJson.getString("name")
                            kabupatenList.add(kabupaten)
                            val kecamatanArray = kabupatenJson.getJSONArray("districts")
                            for (indexKecamatan in 0 until kecamatanArray.length()) {
                                val kecamatanJson = kecamatanArray.getJSONObject(indexKecamatan)
                                val kecamatan = Kecamatan()
                                kecamatan.id = kecamatanJson.getString("id")
                                kecamatan.kabupatenId = kecamatanJson.getString("regency_id")
                                kecamatan.name = kecamatanJson.getString("name")
                                kecamatanList.add(kecamatan)
//                                Log.d("getWilayah", "Id Kecamatan : ${kecamatan.id}")
                                val desaArray = kecamatanJson.getJSONArray("villages")
                                countDesa += desaArray.length()
                                for (indexDesa in 0 until desaArray.length()) {
                                    val desaJson = desaArray.getJSONObject(indexDesa)
                                    val desa = Desa()
                                    desa.id = desaJson.getString("id")
                                    desa.kecamatanId = desaJson.getString("district_id")
                                    desa.name = desaJson.getString("name")
                                    desaList.add(desa)
                                }
                            }
                        }
                    }

                    val offlineViewModel = ViewModelProvider(owner).get(OfflineViewModel::class.java)

                    offlineViewModel.insertProvince(provinceList)
                    offlineViewModel.insertKabupaten(kabupatenList)
                    offlineViewModel.insertKecamatan(kecamatanList)
                    offlineViewModel.insertDesa(desaList)

                }
            }.await()
//                callback.onSuccess(true)
//            val url = "http://dashboard.peers.id/assets/indonesia-region.min.json"
//
//            val jsonObjectRequest = object: JsonObjectRequest(
//                Method.GET, url, null,
//                Response.Listener { response ->
//                    val strResp = response.toString()
//                    Log.d("getWilayah", "String response : $strResp")
//                    val jsonArray = JSONArray(strResp)
//                    val provinceList = ArrayList<Province>()
//                    val kabupatenList = ArrayList<Kabupaten>()
//                    val kecamatanList = ArrayList<Kecamatan>()
////                    val desaList = ArrayList<Desa>()
//                    if (jsonArray.length() > 0) {
//                        for (indexProvince in 0 until jsonArray.length()) {
//                            val provinceJson = jsonArray.getJSONObject(indexProvince)
//                            val province = Province()
//                            province.id = provinceJson.getInt("id")
//                            province.name = provinceJson.getString("name")
//                            provinceList.add(province)
//                            val kabupatenArray = provinceJson.getJSONArray("regencies")
//                            for (indexKabupaten in 0 until kabupatenArray.length()) {
//                                val kabupatenJson = kabupatenArray.getJSONObject(indexKabupaten)
//                                val kabupaten = Kabupaten()
//                                kabupaten.id = kabupatenJson.getInt("id")
//                                kabupaten.provinceId = kabupatenJson.getInt("province_id")
//                                kabupaten.name = kabupatenJson.getString("name")
//                                kabupatenList.add(kabupaten)
//                                val kecamatanArray = kabupatenJson.getJSONArray("districts")
//                                for (indexKecamatan in 0 until kecamatanArray.length()) {
//                                    val kecamatanJson = kecamatanArray.getJSONObject(indexKecamatan)
//                                    val kecamatan = Kecamatan()
//                                    kecamatan.id = kecamatanJson.getInt("id")
//                                    kecamatan.kabupatenId = kecamatanJson.getInt("regency_id")
//                                    kecamatan.name = kecamatanJson.getString("name")
//                                    kecamatanList.add(kecamatan)
//                                    Log.d("getWilayah", "Id Kecamatan : ${kecamatan.id}")
////                                    val desaArray = kecamatanJson.getJSONArray("villages")
////                                    for (indexDesa in 0 until desaArray.length()) {
////                                        val desaJson = desaArray.getJSONObject(indexDesa)
////                                        val desa = Desa()
////                                        desa.id = desaJson.getInt("id")
////                                        desa.kecamatanId = desaJson.getInt("district_id")
////                                        desa.name = desaJson.getString("name")
////                                        desaList.add(desa)
////                                    }
//                                }
//                            }
//                        }
//
//                        val offlineViewModel = ViewModelProvider(owner).get(OfflineViewModel::class.java)
//
//                        GlobalScope.launch(Dispatchers.IO) {
//                            offlineViewModel.insertProvince(provinceList)
//                            offlineViewModel.insertKabupaten(kabupatenList)
//                            offlineViewModel.insertKecamatan(kecamatanList)
////                            offlineViewModel.insertDesa(desaList)
//                        }
//
//                        callback.onSuccess(true)
//                    }
//                },
//                Response.ErrorListener { error ->
//                    //                Log.e("getConfig", error.toString())
//                    val response = error.networkResponse
//                    if (error is ServerError && response != null) {
//                        try {
//                            val res = String(
//                                response.data,
//                                Charset.forName(
//                                    HttpHeaderParser.parseCharset(
//                                        response.headers,
//                                        "utf-8"
//                                    )
//                                )
//                            )
//                            // Now you can use any deserializer to make sense of data
////                        val obj = JSONObject(res);
//                            Log.d("getProvinsi", "RES : $res")
//                        } catch (e1: UnsupportedEncodingException) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (e2: JSONException) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//
//                    }
//                }
//            )
//            {
//                @Throws(AuthFailureError::class)
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
        }

        private fun postCitcall(phoneNumber: String, preferences: SharedPreferences, context: Context, callback: CitcallCallback) {
            val url = "${API_HOSTNAME}member/miscall"

            val params = HashMap<String, String>()
//        params["token"] = preferences.getString("token", null)!!

            params["member_hp"] = phoneNumber

            val parameters = JSONObject(params as Map<*, *>)
            val jsonObjectRequest = object: JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj = JSONObject(strResp)
                    val loginStatus = jsonObj.getString("status")
                    Log.d("postCollection", strResp)
                    if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
//                    popUpSnack(view, "Login Failed!")
                    } else if (loginStatus.toInt() == 200) {
                        val data = jsonObj.getJSONObject("data")
                        if(data.getString("info") == "queued") {
//                        Log.d("postCitcall", "Token : ${data.getString("token")}")
                            callback.onSuccess(data.getString("token"))
                        }
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("postCitcall", error.toString())
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

//        fun getProvince(context: Context, callback: ProvinceCallback/*, apiToken: String*/) {
////        val url = "http://dev.farizdotid.com/api/daerahindonesia/provinsi"
////        val url = "https://x.rajaapi.com/MeP7c5ne${apiToken}/m/wilayah/provinsi"
////            val url = "http://jendela.data.kemdikbud.go.id/api/index.php/CWilayah/wilayahGET?mst_kode_wilayah=000000"
//            val url = "http://dashboard.peers.id/assets/indonesia-region.min.json"
//
//            val jsonObjectRequest = object: JsonObjectRequest(
//                Method.GET, url, null,
//                Response.Listener { response ->
//                    val strResp = response.toString()
//
//                    val jsonObj = JSONObject(strResp)
////                val error = jsonObj.getBoolean("error")
////                Log.d("getProvinsi", strResp)
////                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
////                    popUpSnack(view, "Login Failed!")
////                } else if (loginStatus.toInt() == 201) {
////                }
////                if (!error) {
//                    val data = jsonObj.getJSONArray("data")
//                    val provinces = ArrayList<Province>()
//                    if (data.length() > 0) {
//                        for(index in 0 until data.length()) {
//                            val province = Province()
//                            val obj = data.getJSONObject(index)
////                        province.id = index + 1
//                            province.kodeWilayah = obj.getString("kode_wilayah")
//                            province.nama = obj.getString("nama")
//                            provinces.add(province)
////                            Log.d("getProvinsi", "NAMA PROVINSI : ${province.nama}")
//                        }
//                    }
//                    callback.onSuccess(provinces)
////                }
//                },
//                Response.ErrorListener { error ->
//                    //                Log.e("getConfig", error.toString())
//                    val response = error.networkResponse
//                    if (error is ServerError && response != null) {
//                        try {
//                            val res = String(
//                                response.data,
//                                Charset.forName(
//                                    HttpHeaderParser.parseCharset(
//                                        response.headers,
//                                        "utf-8"
//                                    )
//                                )
//                            )
//                            // Now you can use any deserializer to make sense of data
////                        val obj = JSONObject(res);
//                            Log.d("getProvinsi", "RES : $res")
//                        } catch (e1: UnsupportedEncodingException) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (e2: JSONException) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//
//                    }
//                }
//            )
//            {
//                @Throws(AuthFailureError::class)
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
//
//        }
//
//        fun getKabupaten(context: Context, callback: KabupatenCallback, idProvinsi: String/*, apiToken: String*/) {
////        val url = "http://dev.farizdotid.com/api/daerahindonesia/provinsi/$idProvinsi/kabupaten"
//            val url = "http://jendela.data.kemdikbud.go.id/api/index.php/CWilayah/wilayahGET?mst_kode_wilayah=$idProvinsi"
////        val url = "https://x.rajaapi.com/MeP7c5ne${apiToken}/m/wilayah/kabupaten?idpropinsi=${idProvinsi}"
//
//            val jsonObjectRequest = object: JsonObjectRequest(
//                Method.GET, url, null,
//                Response.Listener { response ->
//                    val strResp = response.toString()
//                    val jsonObj = JSONObject(strResp)
////                val success = jsonObj.getBoolean("success")
////                Log.d("getKabupaten", strResp)
////                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
////                    popUpSnack(view, "Login Failed!")
////                } else if (loginStatus.toInt() == 201) {
////                }
////                if (success) {
//                    val data = jsonObj.getJSONArray("data")
//                    val kabupatens = ArrayList<Kabupaten>()
//                    if (data.length() > 0) {
//                        for(index in 0 until data.length()) {
//                            val kabupaten = Kabupaten()
//                            val obj = data.getJSONObject(index)
////                            kabupaten.id = obj.getInt("id")
//                            kabupaten.kodeWilayah = obj.getString("kode_wilayah")
//                            kabupaten.masterCode = obj.getString("mst_kode_wilayah")
//                            kabupaten.nama = obj.getString("nama")
//                            kabupatens.add(kabupaten)
//                        }
//                    }
//                    callback.onSuccess(kabupatens)
////                }
//                },
//                Response.ErrorListener { error ->
//                    //                Log.e("getConfig", error.toString())
//                    val response = error.networkResponse
//                    if (error is ServerError && response != null) {
//                        try {
//                            val res = String(
//                                response.data,
//                                Charset.forName(
//                                    HttpHeaderParser.parseCharset(
//                                        response.headers,
//                                        "utf-8"
//                                    )
//                                )
//                            )
//                            // Now you can use any deserializer to make sense of data
////                        val obj = JSONObject(res);
//                            Log.d("getKabupaten", "RES : $res")
//                        } catch (e1: UnsupportedEncodingException) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (e2: JSONException) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//
//                    }
//                }
//            )
//            {
//                @Throws(AuthFailureError::class)
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
//
//        }
//
//        fun getKecamatan(context: Context, callback: KecamatanCallback, idKabupaten: String/*, apiToken: String*/) {
////        val url = "http://dev.farizdotid.com/api/daerahindonesia/provinsi/kabupaten/$idKabupaten/kecamatan"
////        val url = "https://x.rajaapi.com/MeP7c5ne${apiToken}/m/wilayah/kelurahan?idkecamatan=${idKabupaten}"
//            val url = "http://jendela.data.kemdikbud.go.id/api/index.php/CWilayah/wilayahGET?mst_kode_wilayah=$idKabupaten"
//
//            val jsonObjectRequest = object: JsonObjectRequest(
//                Method.GET, url, null,
//                Response.Listener { response ->
//                    val strResp = response.toString()
//                    val jsonObj = JSONObject(strResp)
////                val success = jsonObj.getBoolean("success")
//                Log.d("getKecamatan", strResp)
////                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
////                    popUpSnack(view, "Login Failed!")
////                } else if (loginStatus.toInt() == 201) {
////                }
////                if (success) {
//                    val data = jsonObj.getJSONArray("data")
//                    val kecamatans = ArrayList<Kecamatan>()
//                    if (data.length() > 0) {
//                        for(index in 0 until data.length()) {
//                            val kecamatan = Kecamatan()
//                            val obj = data.getJSONObject(index)
////                        kecamatan.id = obj.getInt("id")
//                            kecamatan.masterCode = obj.getString("mst_kode_wilayah")
//                            kecamatan.kodeWilayah = obj.getString("kode_wilayah")
//                            kecamatan.nama = obj.getString("nama")
//                            kecamatans.add(kecamatan)
////                            getDesa(context, callback, kecamatan.id)
//                        }
//                    }
//                    callback.onSuccess(kecamatans)
////                }
//                },
//                Response.ErrorListener { error ->
//                    //                Log.e("getConfig", error.toString())
//                    val response = error.networkResponse
//                    if (error is ServerError && response != null) {
//                        try {
//                            val res = String(
//                                response.data,
//                                Charset.forName(
//                                    HttpHeaderParser.parseCharset(
//                                        response.headers,
//                                        "utf-8"
//                                    )
//                                )
//                            )
//                            // Now you can use any deserializer to make sense of data
////                        val obj = JSONObject(res);
//                            Log.d("getConfig", "RES : $res")
//                        } catch (e1: UnsupportedEncodingException) {
//                            // Couldn't properly decode data to string
//                            e1.printStackTrace();
//                        } catch (e2: JSONException) {
//                            // returned data is not JSONObject?
//                            e2.printStackTrace();
//                        }
//
//                    }
//                }
//            )
//            {
//                @Throws(AuthFailureError::class)
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//            VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
//
//        }
    }

    /*public fun getRajaApiToken(context: Context, callback: ProvinceCallback, preferences: SharedPreferences) {
        val url = "https://x.rajaapi.com/poe"

        val jsonObjectRequest = object: JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                val strResp = response.toString()
                val jsonObj = JSONObject(strResp)
                val success = jsonObj.getBoolean("success")
                if (success) {
                    val token = jsonObj.getString("token")
                    preferences.edit().putString("raja_api_token", token).apply()
                    getProvince(context, callback, token)
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
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }*/

//    public fun getDesa(context: Context, callback: KabupatenCallback, idKecamatan: Int) {
//        val url = "http://dev.farizdotid.com/api/daerahindonesia/provinsi/kabupaten/kecamatan/$idKecamatan/desa"
//
//        val jsonObjectRequest = object: JsonObjectRequest(
//            Method.GET, url, null,
//            Response.Listener { response ->
//                val strResp = response.toString()
//                val jsonObj = JSONObject(strResp)
//                val error = jsonObj.getBoolean("error")
////                Log.d("getDesa", strResp)
////                if (loginStatus.toInt() == 400 || loginStatus.toInt() == 401) {
////                    popUpSnack(view, "Login Failed!")
////                } else if (loginStatus.toInt() == 201) {
////                }
//                if (!error) {
//                    val data = jsonObj.getJSONArray("desas")
//                    val desas = ArrayList<Desa>()
//                    if (data.length() > 0) {
//                        for(index in 0 until data.length()) {
//                            val desa = Desa()
//                            val obj = data.getJSONObject(index)
//                            desa.id = obj.getInt("id")
//                            desa.idKecamatan = obj.getInt("id_kecamatan")
//                            desa.nama = obj.getString("nama")
//                            desas.add(desa)
//                        }
//                    }
////                    callback.onSuccess3(desas)
//                }
//            },
//            Response.ErrorListener { error ->
//                //                Log.e("getConfig", error.toString())
//                val response = error.networkResponse
//                if (error is ServerError && response != null) {
//                    try {
//                        val res = String(
//                            response.data,
//                            Charset.forName(
//                                HttpHeaderParser.parseCharset(
//                                    response.headers,
//                                    "utf-8"
//                                )
//                            )
//                        )
//                        // Now you can use any deserializer to make sense of data
////                        val obj = JSONObject(res);
//                        Log.d("getConfig", "RES : $res")
//                    } catch (e1: UnsupportedEncodingException) {
//                        // Couldn't properly decode data to string
//                        e1.printStackTrace();
//                    } catch (e2: JSONException) {
//                        // returned data is not JSONObject?
//                        e2.printStackTrace();
//                    }
//
//                }
//            }
//        )
//        {
//            @Throws(AuthFailureError::class)
//            override fun getBodyContentType(): String {
//                return "application/json"
//            }
//        }
//        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
//
//    }
}