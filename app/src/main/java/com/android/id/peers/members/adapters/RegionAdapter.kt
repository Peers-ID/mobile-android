package com.android.id.peers.members.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.id.peers.R
import com.android.id.peers.members.models.Desa
import com.android.id.peers.members.models.Kabupaten
import com.android.id.peers.members.models.Kecamatan
import com.android.id.peers.members.models.Province

class RegionAdapter(val context: Context, val regions: List<Any>, val type: String): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.custom_spinner_item, parent, false)
        val spinnerText = rowView.findViewById<TextView>(R.id.spinner_text)
        when (type) {
            "Province" -> {
                spinnerText.text = (regions[position] as Province).nama
            }
            "Kabupaten" -> {
                spinnerText.text = (regions[position] as Kabupaten).nama
            }
            "Kecamatan" -> {
                spinnerText.text = (regions[position] as Kecamatan).nama
            }
            "Desa" -> {
                spinnerText.text = (regions[position] as Desa).nama
            }
            else -> {

            }
        }
        return rowView
    }

    override fun getItem(position: Int): Any {
        return regions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return regions.size
    }

}