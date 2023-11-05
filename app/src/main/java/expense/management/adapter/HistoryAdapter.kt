package expense.management.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import expense.management.HistoryTypes
import expense.management.R
import expense.management.entities.HistoryItem

class HistoryAdapter(private val dataList: MutableList<HistoryItem>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private val TAG = "debugTag"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val item = dataList[position]

        holder.titleExpTV.text = item.name
        holder.countExpTV.text = item.count

        if (item.type == HistoryTypes.INCOME) {
            holder.itemView.setBackgroundResource(R.drawable.history_background_income)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.history_background_expense)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleExpTV: TextView = itemView.findViewById(R.id.history_name)
        val countExpTV: TextView = itemView.findViewById(R.id.history_count)
    }
}