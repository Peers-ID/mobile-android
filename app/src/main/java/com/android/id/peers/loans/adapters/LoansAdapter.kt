package com.android.id.peers.loans.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.id.peers.R
import com.android.id.peers.loans.models.Loan
import com.android.id.peers.util.CurrencyFormat
import kotlinx.android.synthetic.main.layout_loan_disbursement_items.view.*

//class LoansAdapter(val items : ArrayList<LoanItem>, val context : Context,
//                   val type: Int,
//                   val clickListener: (LoanItem) -> Unit) :
class LoansAdapter(val items : ArrayList<Loan>, val context : Context,
                   val type: Int,
                   val clickListener: (Loan) -> Unit) :
    RecyclerView.Adapter<LoansAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Log.d("LoansAdapter", "Item name : ${item.memberName}")
//        holder.loanNo?.text = ""
        holder.memberName?.text = item.memberName
        if(type == 0) {
            holder.totalDisburse?.text = CurrencyFormat.formatRupiah.format(item.totalDisbursed)
        } else {
            val temp = ": ${item.cicilanKe} dari Total : ${item.tenor}"
//            val temp = ": ${CurrencyFormat.formatRupiah.format(item.cicilanPerBulan)} dari Total : ${CurrencyFormat.formatRupiah.format(item.totalDisbursed)}"/* + ", \nSukarela : " + String.format("%d", item.sukarela)*/
            holder.totalDisburse?.text = temp
        }
        (holder as ViewHolder).bind(items[position], clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(from(context).inflate( R.layout.layout_loan_disbursement_items, parent, false))
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Holds the TextView that will add each animal to
//        val loanNo = itemView.loan_no
        val memberName = itemView.member_name
        val totalDisburse = itemView.total_disburse
        //        fun bind(loan: LoanItem, clickListener: (LoanItem) -> Unit) {
//            itemView.setOnClickListener { clickListener(loan) }
//        }
        fun bind(loan: Loan, clickListener: (Loan) -> Unit) {
            itemView.setOnClickListener { clickListener(loan) }
        }
    }
}