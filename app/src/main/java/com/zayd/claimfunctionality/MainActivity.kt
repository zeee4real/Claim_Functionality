package com.zayd.claimfunctionality

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zayd.claimfunctionality.models.Claim
import com.zayd.claimfunctionality.models.Claimfieldoption
import com.zayd.claimfunctionality.models.ClaimsModel
import com.zayd.claimfunctionality.models.Claimtypedetail

class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapter: MainAdapter
    var map = emptyMap<Int, List<Claimfieldoption>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val model = getModel()

        model.Claims.forEach {
            it.Claimtypedetail.forEach{
                if(it.Claimfield.isdependant.toInt() == 1) {
                    map = it.Claimfield.Claimfieldoption.groupBy {i -> i.belongsto!!.toString().toInt() }
                }
            }
        }
        setupMainSpinner(model)
    }

    private fun setupRecyclerView(list: List<Claimtypedetail>){
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.apply {
            mainAdapter = MainAdapter()
            mainAdapter.setData(list, map)
            adapter = mainAdapter
        }
    }

    private fun setupMainSpinner(model: ClaimsModel) {
        val spinner: Spinner = findViewById(R.id.mainDropDown)
        val adapter: ArrayAdapter<Claim> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            model.Claims
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                setupRecyclerView(model.Claims[position].Claimtypedetail)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getModel(): ClaimsModel {
        val string: String = assets.open("claims_json.txt").bufferedReader().use {
            it.readText()
        }
        val typeModel = object : TypeToken<ClaimsModel>() {}.type
        return Gson().fromJson(string, typeModel)
    }

}