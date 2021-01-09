package com.zayd.claimfunctionality

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zayd.claimfunctionality.dialogs.DropDownDialog
import com.zayd.claimfunctionality.models.Claimfieldoption
import com.zayd.claimfunctionality.models.Claimtypedetail
import com.zayd.claimfunctionality.utils.AppUtils

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = ArrayList<Claimtypedetail>()
    private var map = emptyMap<Int, List<Claimfieldoption>>()

    companion object {
        const val DropDown = "DropDown"
        const val SingleLineTextAllCaps = "SingleLineTextAllCaps"
        const val SingleLineText = "SingleLineText"
        const val SingleLineTextNumeric = "SingleLineTextNumeric"

        const val DropDownView = 0
        const val SingleLineTextAllCapsView = 1
        const val SingleLineTextView = 2
        const val SingleLineTextNumericView = 3

        private var selectedIds = HashMap<Int, Int>()
        private var dependantRows = HashMap<Int, Int>()
        private var claimfieldoptionItem: Claimfieldoption? = null
    }

    fun setData(list: List<Claimtypedetail>, map: Map<Int, List<Claimfieldoption>>) {
        this.list = ArrayList(list)
        this.map = map
        list.forEach {
            if (it.Claimfield.type == DropDown)
                selectedIds[it.id.toInt()] = 0
            if (it.Claimfield.isdependant.toInt() == 1)
                it.Claimfield.Claimfieldoption.forEach { option ->
                    dependantRows[option.belongsto.toString().toInt()] = 0
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DropDownView -> DropDownItem.from(parent)
            SingleLineTextView -> SingleLineTextItem.from(parent)
            SingleLineTextAllCapsView -> SingleLineTextCapsItem.from(parent)
            SingleLineTextNumericView -> SingleLineTextNumItem.from(parent)
            else -> SingleLineTextItem.from(parent)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = list[position]
        when (holder.itemViewType) {
            DropDownView -> {
                val localHolder = holder as DropDownItem
                localHolder.bind(currentItem, claimfieldoptionItem, position)
//                localHolder.bindSelected(currentItem.Claimfield.Claimfieldoption[0], position)
                localHolder.itemView.setOnClickListener {
                    val dialog = DropDownDialog(
                        localHolder.itemView.context,
                        currentItem.Claimfield.Claimfieldoption,
                        currentItem.Claimfield.isdependant.toInt() == 1, selectedIds,
                        object : DropDownDialog.DropDownDialogInterface {
                            override fun onItemClick(parentId: Int, item: Claimfieldoption) {
                                localHolder.bindSelected(item, position)
                                if (item.belongsto == null) {
                                    claimfieldoptionItem = item
                                    if (dependantRows.containsKey(item.id.toInt())) {
                                        dependantRows[item.id.toInt()]?.let { it1 ->
                                            notifyItemChanged(
                                                it1
                                            )
                                        }
                                    }
                                }
                            }
                        }, map
                    )
                    dialog.show()
                    AppUtils.dialogCustomise(dialog.window)
                }
            }
            SingleLineTextView -> {
                (holder as SingleLineTextItem).bind(currentItem)
            }
            SingleLineTextAllCapsView -> {
                (holder as SingleLineTextCapsItem).bind(currentItem)
            }
            SingleLineTextNumericView -> {
                (holder as SingleLineTextNumItem).bind(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return when (item.Claimfield.type) {
            DropDown -> 0
            SingleLineTextAllCaps -> 1
            SingleLineText -> 2
            SingleLineTextNumeric -> 3
            else -> -1
        }
    }

    class SingleLineTextItem constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            fun from(parent: ViewGroup) = SingleLineTextItem(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_text, parent, false)
            )
        }

        private var editItem: EditText? = null
        fun bind(claimtypedetail: Claimtypedetail) {
            if (editItem == null) editItem = itemView.findViewById(R.id.editText)
            editItem?.hint = claimtypedetail.Claimfield.label
        }
    }


    class SingleLineTextCapsItem constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup) = SingleLineTextCapsItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_text_caps, parent, false)
            )
        }

        private var editItem: EditText? = null
        fun bind(claimtypedetail: Claimtypedetail) {
            if (editItem == null) editItem = itemView.findViewById(R.id.editText)
            editItem?.hint = claimtypedetail.Claimfield.label
        }
    }

    class SingleLineTextNumItem constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup) = SingleLineTextNumItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_number, parent, false)
            )
        }

        private var editItem: EditText? = null
        fun bind(claimtypedetail: Claimtypedetail) {
            if (editItem == null) editItem = itemView.findViewById(R.id.editText)
            editItem?.hint = claimtypedetail.Claimfield.label
        }
    }


    class DropDownItem constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup) = DropDownItem(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_dropdown, parent, false)
            )
        }

        private var label: TextView? = null
        private var data: TextView? = null
        fun bind(claimtypedetail: Claimtypedetail, item: Claimfieldoption?, position: Int) {
            if (label == null) label = itemView.findViewById(R.id.label)
            if (data == null) data = itemView.findViewById(R.id.data)
            if (data?.text.isNullOrEmpty())
                data?.text = claimtypedetail.Claimfield.Claimfieldoption[0].name
            label?.text = claimtypedetail.Claimfield.label
            if (item == null)
                dataChange(null, claimtypedetail.Claimfield.Claimfieldoption[0], position)
            else {
                dataChange(claimtypedetail, item, position)
                claimfieldoptionItem = null
            }
            if (claimtypedetail.Claimfield.required.toInt() == 1) label?.append("*")
        }

        private fun dataChange(
            claimtypedetail: Claimtypedetail?,
            item: Claimfieldoption,
            position: Int
        ) {
            if (claimtypedetail != null) {
                var dataSet = false
                run loop@{
                    claimtypedetail.Claimfield.Claimfieldoption.forEach { claimfield ->
                        claimfield.belongsto?.let { belongs ->
                            if (belongs == item.id) {
                                data?.text = claimfield.name
                                dataSet = true
                                return@loop
                            }
                        }
                    }
                }
//                if (!dataSet) {
//                    data?.text = ""
//                }
            }
            selectedIds[item.claimfield_id.toInt()] = item.id.toInt()
            if (item.belongsto != null) {
                changeDependantRows(position)
            }
        }

        private fun changeDependantRows(position: Int) {
            dependantRows.forEach { (key, _) ->
                dependantRows[key] = position
            }
        }

        fun bindSelected(item: Claimfieldoption, position: Int) {
            data?.text = item.name
            selectedIds[item.claimfield_id.toInt()] = item.id.toInt()
            if (item.belongsto != null)
                changeDependantRows(position)
        }
    }
}