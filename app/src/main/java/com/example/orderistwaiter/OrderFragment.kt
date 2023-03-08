package com.example.orderistwaiter

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.row_order.view.*
import kotlinx.android.synthetic.main.dialog_order_submission.*
import com.google.gson.GsonBuilder


class OrderFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val database = FirebaseDatabase.getInstance()
        val myOrdersRef = database.getReference("Orders")
        val myMenuRef = database.getReference("Menus")
        val t = inflater.inflate(R.layout.fragment_order, container, false)

        // Dropdown Table
        var tableText = ""
        var tableNo = 0
        val tableArray = resources.getStringArray(R.array.tableDropdown)
        val spinner = t.findViewById<Spinner>(R.id.dropdownTable)
        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.spinner_item, tableArray) } as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    tableText = ""
                }
                else {
                    tableText = parent?.getItemAtPosition(position).toString()
                    tableNo = position
                }

            }

        }

        // Order list View
        val menuList = ArrayList<Menu>()
        var qtyList = MutableList(menuList.size) { 0 }

        val adapter = MenuAdapter(menuList, qtyList)
        val menuView = t.findViewById<RecyclerView>(R.id.orderView)
        menuView.layoutManager = LinearLayoutManager(context)
        menuView.adapter = adapter

        myMenuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the current list of menus
                menuList.clear()
                qtyList.clear()

                // Get list of Menu objects from the dataSnapshot
                for (childSnapshot in dataSnapshot.children) {
                    val menu = childSnapshot.getValue(Menu::class.java)
                    menu?.id = childSnapshot.key
                    if (menu != null) {
                        if (menu.isVisible == true) {
                            menuList.add(menu!!)
                            qtyList.add(0)
                        }
                    }

                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })


        // Submit btn
        val orderSubmitBtn = t.findViewById<Button>(R.id.orderSubmitBtn)
        orderSubmitBtn.setOnClickListener {

            if (tableText == "") {
                Toast.makeText(activity,"Please Select A Table.", Toast.LENGTH_LONG).show()
            }
            else if (qtyList.reduce { acc, i -> acc + i } == 0) {
                Toast.makeText(activity,"There is no order.", Toast.LENGTH_LONG).show()
            }
            else {
                // Pop Up dialog
                val dialogBinding = layoutInflater.inflate(R.layout.dialog_order_submission,null)

                //order List View
                val orderViewDialog = dialogBinding.findViewById<RecyclerView>(R.id.orderViewDialog)
                val filteredMenuList = menuList.filterIndexed { index, _ -> qtyList[index] != 0 }
                val filteredQtyList = qtyList.filter { it != 0 }
                orderViewDialog.layoutManager = LinearLayoutManager(context)
                orderViewDialog.adapter = OrderAdapter(filteredMenuList, filteredQtyList)

                val orderDialog = Dialog(requireContext())

                val orderDialogTitle = dialogBinding.findViewById<TextView>(R.id.orderDialogTitle)
                orderDialogTitle.text = tableText


//                orderDialog.setContentView(R.layout.dialog_order_submission)
                orderDialog.setContentView(dialogBinding)
                orderDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                orderDialog.setCancelable(true)
                orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                orderDialog.show()


                // Confirm button
                val orderDialogConfirmBtn = orderDialog.findViewById<Button>(R.id.orderDialogConfirmBtn)
                orderDialogConfirmBtn.setOnClickListener {
                    Log.d("Order", tableText)

                    var order = Orders(tableNo, ArrayList<Food>())

                    for (i in filteredMenuList.indices) {
                        Log.d("Order", "${filteredMenuList[i]}: ${filteredQtyList[i]}")
                        var food = filteredMenuList[i].name?.let { it1 -> Food(it1, filteredQtyList[i]) }
                        if (food != null) {
                            order.orders.add(food)
                        }
                    }


                    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                    val jsonTutPretty: String = gsonPretty.toJson(order)
                    println(jsonTutPretty)
//                    Log.d("MyOrder",  order.toString())


                    val newOrderRef = myOrdersRef.push() // generate a new key for the new menu
                    val newOrder = order
                    newOrderRef.setValue(newOrder)



                    Toast.makeText(activity,"Order Sent", Toast.LENGTH_LONG).show()
                    orderDialog.dismiss()
                    // reset page
                    tableText = ""
                    spinner.setSelection(0);
                    qtyList = MutableList(menuList.size) { 0 }
                    menuView.adapter = MenuAdapter(menuList, qtyList)
                }
            }
        }
        return t
    }

    // menu list view functions
    inner class MenuHolder(view: View): RecyclerView.ViewHolder(view){
        val menuText = itemView.menuText
        var quantityInput = itemView.quantityInput
    }
    inner class MenuAdapter (var menuList: ArrayList<Menu>, var qtyList: MutableList<Int>): RecyclerView.Adapter<MenuHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
            val view = layoutInflater.inflate(R.layout.row_order, parent, false)
            return MenuHolder(view)
        }
        override fun onBindViewHolder(holder: MenuHolder, @SuppressLint("RecyclerView") position: Int) {
            holder.menuText.text   = menuList[position].name

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


    // Order dialog recycler view
    inner class OrderAdapter (var menuList: List<Menu>, var qtyList: List<Int>): RecyclerView.Adapter<MenuHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
            val view = layoutInflater.inflate(R.layout.row_order, parent, false)
            return MenuHolder(view)
        }
        override fun onBindViewHolder(holder: MenuHolder, @SuppressLint("RecyclerView") position: Int) {

            holder.menuText.text = menuList[position].name
            holder.quantityInput.setText(qtyList[position].toString())
            holder.quantityInput.inputType = InputType.TYPE_NULL

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