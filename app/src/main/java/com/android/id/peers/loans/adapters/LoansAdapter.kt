package com.android.id.peers.loans.adapters

import android.content.Context
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.id.peers.R
import com.android.id.peers.loans.models.LoanItem
import kotlinx.android.synthetic.main.layout_loan_disbursement_items.view.*

class LoansAdapter(val items : ArrayList<LoanItem>, val context : Context) :
    RecyclerView.Adapter<LoansAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.loanNo?.text = item.loanNo
        holder.memberName?.text = item.memberName
        holder.totalDisburse?.text = String.format("%d", item.disburseAmount)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(from(context).inflate( R.layout.layout_loan_disbursement_items, parent, false))
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val loanNo = view.loan_no
        val memberName = view.member_name
        val totalDisburse = view.total_disburse
    }
}