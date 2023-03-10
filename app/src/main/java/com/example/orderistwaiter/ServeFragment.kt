package com.example.orderistwaiter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.row_serve.view.*


class ServeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val t = inflater.inflate(R.layout.fragment_serve, container, false)

        // Ready To Serve
//        val orderList = mutableListOf <Orders>()
//        orderList.add(Orders(1, arrayListOf(Food("Chicken Fried Rice", 1, "Ready to Serve"))))
//        orderList.add(Orders(2, arrayListOf(Food("Beef Noodle Soup", 2, "Rejected"), Food("Egg Fried Rice", 1, "Ready to Serve"))))
//        orderList.add(Orders(3, arrayListOf(Food("Vegetable Stir Fry", 3, "Ready to Serve"), Food("Steamed Rice", 1, "Rejected"),Food("Beef Noodle Soup", 2, "Ready to Serve"))))
//
        val database = FirebaseDatabase.getInstance()
        val myOrdersRef = database.getReference("Orders")



        val filteredOrders = ArrayList<Serve>()
        val rejectOrders = ArrayList<Serve>()
        val adapter = ServeAdapter(filteredOrders)
        val serveReadyView = t.findViewById<RecyclerView>(R.id.serveReadyView)
        serveReadyView.layoutManager = LinearLayoutManager(context)
        serveReadyView.adapter = adapter

        val adapter1 = ServeAdapter(rejectOrders)
        val serveRejectView = t.findViewById<RecyclerView>(R.id.serveRejectView)
        serveRejectView.layoutManager = LinearLayoutManager(context)
        serveRejectView.adapter = adapter1

        myOrdersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the current list of orders
                filteredOrders.clear()
                rejectOrders.clear()
                // Get list of Order objects from the dataSnapshot
                for (childSnapshot in dataSnapshot.children) {
                    val order = childSnapshot.getValue(Orders::class.java)
                    if (order != null) {
                        order.id = childSnapshot.key.toString()
                        for ((index, orderItem) in order.orders.withIndex()) {
                            if (orderItem?.status == "Ready to Serve") {
                                filteredOrders.add(Serve(order.tableNo, orderItem,childSnapshot.key.toString(), index))
                            } else if (orderItem?.status == "Rejected") {
                                rejectOrders.add(Serve(order.tableNo, orderItem,childSnapshot.key.toString(), index))
                            }
                        }
                    }
                }


                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
                adapter1.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return t
    }

    // Serve View List
    inner class ServeHolder(view: View): RecyclerView.ViewHolder(view){
        val serveTable = itemView.serveTable
        val serveMenu = itemView.serveMenu
        val serveQty = itemView.serveQty
        val serveAct = itemView.serveAct
    }
    inner class ServeAdapter (var serveOrders: List<Serve>): RecyclerView.Adapter<ServeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServeHolder {
            val view = layoutInflater.inflate(R.layout.row_serve, parent, false)
            return ServeHolder(view)
        }
        override fun onBindViewHolder(holder: ServeHolder, position: Int) {
            holder.serveTable.text = serveOrders[position].tableNo.toString()
            holder.serveMenu.text = serveOrders[position].food.name
            holder.serveQty.text = serveOrders[position].food.quantity.toString()


            holder.serveAct.setOnClickListener {
                // pop up dialog
                val dialogBinding = layoutInflater.inflate(R.layout.dialog_confirmation,null)
                val orderDialog = Dialog(requireContext())
                orderDialog.setContentView(R.layout.dialog_confirmation)
                orderDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                orderDialog.setCancelable(true)
                orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val confirmationTitle = orderDialog.findViewById<TextView>(R.id.confirmationTitle)
                val confirmationDescription = orderDialog.findViewById<TextView>(R.id.confirmationDescription)

                if (serveOrders[position].food.status == "Rejected") {
                    confirmationTitle.text = "Already Rejected"
                    confirmationDescription.text = serveOrders[position].food.name + " must be informed to table "+ serveOrders[position].tableNo.toString()+" before completing action."
                }
                else {
                    confirmationTitle.text = "Already Served"
                    confirmationDescription.text = serveOrders[position].food.name + " has been successfully served to table "+ serveOrders[position].tableNo.toString()+"?"
                }

                orderDialog.show()

                val dismissButton = orderDialog.findViewById<Button>(R.id.dismissButton)
                dismissButton.setOnClickListener {
                    orderDialog.dismiss()
                }
                val confirmButton = orderDialog.findViewById<Button>(R.id.confirmButton)
                confirmButton.setOnClickListener {
                    println("Complete")
                    val database = FirebaseDatabase.getInstance()
                    database.getReference("Orders/${serveOrders[position].id}/orders/${serveOrders[position].index}/status").setValue("Complete")
                    orderDialog.dismiss()
                }


            }

        }
        override fun getItemCount(): Int {
            return serveOrders.size
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}