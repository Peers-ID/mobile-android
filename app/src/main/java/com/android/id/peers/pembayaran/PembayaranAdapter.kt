package com.android.id.peers.pembayaran

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.id.peers.R
import com.android.id.peers.anggota.StatusPinjaman
import kotlinx.android.synthetic.main.layout_pembayaran_item.view.*

class PembayaranAdapter(private val items : ArrayList<StatusPinjaman>, val context : Context,
                        private val clickListener: (StatusPinjaman) -> Unit) :
    RecyclerView.Adapter<PembayaranAdapter.ViewHolder>()  {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Log.d("LoansAdapter", "Item name : ${item.namaLengkap}")
        holder.namaLengkap.text = item.namaLengkap
        holder.jatuhTempo.text = item.tanggal
        holder.bind(items[position], clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate( R.layout.layout_pembayaran_item, parent, false))
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaLengkap: TextView = itemView.nama_anggota
        val jatuhTempo: TextView = itemView.jatuh_tempo
        fun bind(statusPinjaman: StatusPinjaman, clickListener: (StatusPinjaman) -> Unit) {
            itemView.setOnClickListener { clickListener(statusPinjaman) }
        }
    }
}