package com.vksssd.alpha.ui.main.history

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vksssd.alpha.data.entity.Transaction
import com.vksssd.alpha.data.entity.TransactionType
import com.vksssd.alpha.databinding.ItemTranscationBinding
import java.util.Locale


class HistoryAdapter : ListAdapter<Transaction, HistoryAdapter.HistoryViewHolder>(
    TransactionDiffCallback()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemTranscationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Transaction>?) {
        Log.d("HistoryAdapter", "submitList() called with list size: ${list?.size}")
        super.submitList(list)
    }

    class HistoryViewHolder(private val binding: ItemTranscationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.getDefault())

        fun bind(item: Transaction?) {
            binding.apply{
                transactionType.text = when( item?.type) {
                    TransactionType.CASH_IN -> "By Cash"
                    TransactionType.UPI_PAYMENT -> "Payment on UPI App"
                    TransactionType.BILL_PAYMENT -> "Bill In Cart"
                    TransactionType.REFUND -> "Refund"
                    else -> "Unknown"
                }
                transactionAmount.text = item?.amount.toString()
                transactionDate.text = dateFormat.format(item?.timestamp)
                applicationTv.text = when(item?.appName){
                    "ALPHA" -> "Payment on Alpha"
                    else -> "Payment on "+item?.appName
                }

                val amountColor = when (item?.type) {
                    TransactionType.CASH_IN -> android.graphics.Color.GREEN
                    TransactionType.UPI_PAYMENT -> android.graphics.Color.BLUE
                    TransactionType.BILL_PAYMENT, TransactionType.REFUND -> android.graphics.Color.RED
                    else -> android.graphics.Color.BLACK
                }
                transactionCard.setCardBackgroundColor(amountColor)
            }
        }

    }

    private class TransactionDiffCallback:DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.transactionId == newItem.transactionId
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }

}
