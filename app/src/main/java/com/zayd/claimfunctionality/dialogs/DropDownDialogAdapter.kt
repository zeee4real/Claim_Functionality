package com.zayd.claimfunctionality.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zayd.claimfunctionality.models.Claimfieldoption

class DropDownDialogAdapter(private val callBack: DropDownAdapterInterface) : ListAdapter<Claimfieldoption, RecyclerView.ViewHolder>(
    DropDownAdapterDiffUtil()
) {

    interface DropDownAdapterInterface {
        fun onItemClick(parentId: Int, item: Claimfieldoption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DropDownViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        val localHolder = holder as DropDownViewHolder
        localHolder.bind(currentItem.name)
        localHolder.itemView.setOnClickListener {
            callBack.onItemClick(currentItem.id.toInt(), currentItem)
        }
    }

    class DropDownViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup) = DropDownViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false
                )
            )
        }

        private var textView: TextView? = null

        fun bind(value: String){
            if(textView == null) textView = itemView.findViewById(android.R.id.text1)
            textView?.text = value
        }
    }
}

class DropDownAdapterDiffUtil: DiffUtil.ItemCallback<Claimfieldoption>() {
    override fun areItemsTheSame(oldItem: Claimfieldoption, newItem: Claimfieldoption): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Claimfieldoption, newItem: Claimfieldoption): Boolean {
        return oldItem.id == newItem.id
    }
}