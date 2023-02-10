package com.example.orderistwaiter

import android.os.Bundle
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
        val tableArray = resources.getStringArray(R.array.tableDropdown)
        val t = inflater.inflate(R.layout.fragment_order, container, false)
        val spinner = t.findViewById<Spinner>(R.id.dropdownTable)
        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, androidx.transition.R.layout.support_simple_spinner_dropdown_item, tableArray) } as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                println(type)
            }

        }

        // Order list View
        val orderView = t.findViewById<RecyclerView>(R.id.orderView)
        orderView.layoutManager = LinearLayoutManager(context)
        orderView.adapter = MenuAdapter(sampleMessage())

        return t
    }

    fun sampleMessage(): List<String> {
        val menuList = listOf(
            "Chicken Fried Rice",
            "Tomyam Kung",
            "SomTam Thai",
            "Masala Chai",
            "Kulche",
            "Dhoka",
            "Jalebi",
            "Pizza Hawaiian",
            "SomTam Thai",
            "Masala Chai",
            "Kulche",
            "Dhoka",
            "Jalebi",
            "Pizza Hawaiian",
            "SomTam Thai",
            "Masala Chai",
            "Kulche",
            "Dhoka",
            "Jalebi",
            "Pizza Hawaiian"
        )
        return menuList
    }

    inner class MenuHolder(view: View): RecyclerView.ViewHolder(view){
        val menuText = itemView.menuText

    }

    inner class MenuAdapter (var menuList: List<String>): RecyclerView.Adapter<MenuHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
            val view = layoutInflater.inflate(R.layout.order_row, parent, false)
            return MenuHolder(view)
        }


        override fun onBindViewHolder(holder: MenuHolder, position: Int) {
            holder.menuText.text   = menuList[position]
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