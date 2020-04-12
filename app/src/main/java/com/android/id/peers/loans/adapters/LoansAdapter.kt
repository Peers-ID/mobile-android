package com.android.id.peers.loans.adapters

import android.content.Context
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.id.peers.R
import com.android.id.peers.loans.models.LoanItem
import kotlinx.android.synthetic.main.layout_loan_disbursement_items.view.*

class LoansAdapter(val items : ArrayList<LoanItem>, val context : Context,
                   val type: Int,
                   val clickListener: (LoanItem) -> Unit) :
    RecyclerView.Adapter<LoansAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.loanNo?.text = item.loanNo
        holder.memberName?.text = item.memberName
        if(type == 0) {
            holder.totalDisburse?.text = String.format("%d", item.disburseAmount)
        } else {
            val temp = ": " + String.format("%d", item.cicilan) + " dari Total : " + String.format("%d", item.disburseAmount)/* + ", \nSukarela : " + String.format("%d", item.sukarela)*/
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
        val loanNo = itemView.loan_no
        val memberName = itemView.member_name
        val totalDisburse = itemView.total_disburse
        fun bind(loan: LoanItem, clickListener: (LoanItem) -> Unit) {
            itemView.setOnClickListener { clickListener(loan) }
        }
    }
}