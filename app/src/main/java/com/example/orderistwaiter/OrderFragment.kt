package com.example.orderistwaiter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.order_row.view.*


class OrderFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Dropdown Table
        var tableNo = ""
        val tableArray = resources.getStringArray(R.array.tableDropdown)
        val t = inflater.inflate(R.layout.fragment_order, container, false)
        val spinner = t.findViewById<Spinner>(R.id.dropdownTable)
        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.spinner_item, tableArray) } as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    tableNo = ""
                }
                else {
                    tableNo = parent?.getItemAtPosition(position).toString()
                }

            }

        }

        // Order list View
        val menuList = listOf(
            "Chicken Fried Rice",
            "Tomyam Kung",
            "SomTam Thai",
            "Masala Chai",
            "Kulche",
            "Dhoka",
            "Jalebi",
            "Pizza Hawaiian"
        )
        val qtyList = MutableList(menuList.size) { 0 }

        val orderView = t.findViewById<RecyclerView>(R.id.orderView)
        orderView.layoutManager = LinearLayoutManager(context)
        orderView.adapter = MenuAdapter(menuList, qtyList)

        // Submit btn
        val orderSubmitBtn = t.findViewById<Button>(R.id.orderSubmitBtn)
        orderSubmitBtn.setOnClickListener {
            Log.d("Order", tableNo)
            for (i in qtyList.indices) {
                Log.d("Order", "${menuList[i]}: ${qtyList[i]}")
            }
            if (tableNo == "") {
                Toast.makeText(activity,"Please Select A Table", Toast.LENGTH_LONG).show()
            }
            else {
                // next Move
            }
        }



        return t
    }





    inner class MenuHolder(view: View): RecyclerView.ViewHolder(view){
        val menuText = itemView.menuText
        var quantityInput = itemView.quantityInput


    }

    inner class MenuAdapter (var menuList: List<String>, var qtyList: MutableList<Int>): RecyclerView.Adapter<MenuHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
            val view = layoutInflater.inflate(R.layout.order_row, parent, false)
            return MenuHolder(view)
        }


        override fun onBindViewHolder(holder: MenuHolder, @SuppressLint("RecyclerView") position: Int) {
            holder.menuText.text   = menuList[position]

            holder.quantityInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    val newValue = p0.toString().toIntOrNull()
                    if (newValue != null) {
                        qtyList[position] = newValue
                    }
                }
                // other methods are not necessary for this example
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

        }

        override fun getItemCount(): Int {
            return menuList.size
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroyView() {
        super.onDestroyView()
    }

}