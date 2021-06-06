package com.android.id.peers.util.response

import com.google.gson.JsonArray
import org.json.JSONObject

data class ApiResponse(val data: Any,
                       val status:Int,
                       val message:String)
