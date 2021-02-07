package com.android.id.peers.simpanan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.id.peers.R
import com.android.id.peers.simpanan.ExpandableRecyclerViewAdapter
import com.android.id.peers.simpanan.Member
import com.android.id.peers.simpanan.Simpanan
import com.android.id.peers.util.CurrencyFormat
import kotlinx.android.synthetic.main.layout_child_row.*
import kotlinx.android.synthetic.main.layout_parent_row.*

class SimpananItemAdapter(parents: ArrayList<Member>) :
    ExpandableRecyclerViewAdapter<Simpanan, Member, SimpananItemAdapter.PViewHolder, SimpananItemAdapter.CViewHolder>(
        parents, ExpandingDirection.VERTICAL
    ) {

    override fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): PViewHolder {

        return PViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_parent_row,
                parent,
                false
            )
        )
    }


    override fun onCreateChildViewHolder(child: ViewGroup, viewType: Int): CViewHolder {
        return CViewHolder(
            LayoutInflater.from(child.context).inflate(
                R.layout.layout_child_row,
                child,
                false
            )
        )
    }

    override fun onBindParentViewHolder(
        parentViewHolder: PViewHolder,
        expandableType: Member,
        position: Int
    ) {
        parentViewHolder.nama_anggota.text = expandableType.name
    }

    override fun onBindChildViewHolder(
        childViewHolder: CViewHolder,
        expandedType: Simpanan,
        expandableType: Member,
        position: Int
    ) {
        childViewHolder.produk_pinjaman.text = expandedType.namaProduct
        childViewHolder.status_pinjaman.text = expandedType.statusPinjaman
        childViewHolder.simpanan_pokok.text = CurrencyFormat.formatRupiah.format(expandedType.simpananPokok)
        childViewHolder.simpanan_wajib.text = CurrencyFormat.formatRupiah.format(expandedType.simpananWajib)
        childViewHolder.simpanan_sukarela.text = CurrencyFormat.formatRupiah.format(expandedType.simpananSukarela)
        val totalSimpananText = "Total Simpanan " + CurrencyFormat.formatRupiah.format(expandedType.totalSimpanan)
        childViewHolder.total_simpanan.text = totalSimpananText
    }




    override fun onExpandedClick(
        expandableViewHolder: PViewHolder,
        expandedViewHolder: CViewHolder,
        expandedType: Simpanan,
        expandableType: Member
    ) {
        expandedViewHolder.produk_pinjaman.text = expandedType.namaProduct
        expandedViewHolder.status_pinjaman.text = expandedType.statusPinjaman
        expandedViewHolder.simpanan_pokok.text = CurrencyFormat.formatRupiah.format(expandedType.simpananPokok)
        expandedViewHolder.simpanan_wajib.text = CurrencyFormat.formatRupiah.format(expandedType.simpananWajib)
        expandedViewHolder.simpanan_sukarela.text = CurrencyFormat.formatRupiah.format(expandedType.simpananSukarela)
        val totalSimpananText = "Total Simpanan " + CurrencyFormat.formatRupiah.format(expandedType.totalSimpanan)
        expandedViewHolder.total_simpanan.text = totalSimpananText
//        Toast.makeText(
//            expandableViewHolder.containerView.context,
//            expandableType.name + " " + expandedType.name + " Position: " + expandedViewHolder.adapterPosition,
//            Toast.LENGTH_SHORT
//        ).show()
    }

    override fun onExpandableClick(
        expandableViewHolder: PViewHolder,
        expandableType: Member
    ) {
//        Toast.makeText(
//            expandableViewHolder.containerView.context,
//            expandableType.name + " Position: " + expandableViewHolder.adapterPosition,
//            Toast.LENGTH_SHORT
//        ).show()
    }

    class PViewHolder(v: View) : ExpandableRecyclerViewAdapter.ExpandableViewHolder(v)

    class CViewHolder(v: View) : ExpandableRecyclerViewAdapter.ExpandedViewHolder(v)
}