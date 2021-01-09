package com.zayd.claimfunctionality.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zayd.claimfunctionality.R
import com.zayd.claimfunctionality.models.Claimfieldoption


class DropDownDialog(
    context: Context,
    private var items: List<Claimfieldoption>?,
    private val isDependant: Boolean = false,
    private val totalDropDowns: HashMap<Int, Int>,
    private val callBack: DropDownDialogInterface,
    private val map: Map<Int, List<Claimfieldoption>>
) : Dialog(context) {

    interface DropDownDialogInterface {
        fun onItemClick(parentId: Int, item: Claimfieldoption)
    }

    private lateinit var dropDownDialogAdapter: DropDownDialogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_drop_down)
        if (isDependant) {
            items = null
            totalDropDowns.forEach { (_, value) ->
                if(map.containsKey(value)) {
                    items = map.getValue(value)
                    return@forEach
                }
            }
        }
        initRecyclerView(items)
        val close: TextView = findViewById(R.id.close)
        close.setOnClickListener {
            dismiss()
        }
        val searchText: EditText = findViewById(R.id.searchText)
        if(items!=null) {
            searchText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(data: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (data.toString().isNotEmpty()) {
                        val filteredList = ArrayList<Claimfieldoption>()
                        items!!.forEach {
                            if (it.name.contains(data.toString(), true))
                                filteredList.add(it)
                        }
                        submitList(filteredList)
                    } else {
                        submitList(items!!)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    private fun submitList(filteredList: List<Claimfieldoption>) {
        dropDownDialogAdapter.submitList(filteredList)
        dropDownDialogAdapter.notifyDataSetChanged()
    }

    private fun initRecyclerView(items: List<Claimfieldoption>?) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        if(items!=null) {
            recyclerView.apply {
                dropDownDialogAdapter =
                    DropDownDialogAdapter(object : DropDownDialogAdapter.DropDownAdapterInterface {
                        override fun onItemClick(parentId: Int, item: Claimfieldoption) {
                            callBack.onItemClick(parentId, item)
                            dismiss()
                        }
                    })
                dropDownDialogAdapter.submitList(items)
                adapter = dropDownDialogAdapter
            }
        }
    }
}