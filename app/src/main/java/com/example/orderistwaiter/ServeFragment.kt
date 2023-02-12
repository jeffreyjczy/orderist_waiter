package com.example.orderistwaiter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.row_history.view.*
import kotlinx.android.synthetic.main.row_serve.view.*


class ServeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val t = inflater.inflate(R.layout.fragment_serve, container, false)

        // Ready To Serve
        val orderList = mutableListOf <Orders>()
        orderList.add(Orders(1, arrayListOf(Food("Chicken Fried Rice", 1, "Ready to Serve"))))
        orderList.add(Orders(2, arrayListOf(Food("Beef Noodle Soup", 2), Food("Egg Fried Rice", 1, "Ready to Serve"))))
        orderList.add(Orders(3, arrayListOf(Food("Vegetable Stir Fry", 3, "Ready to Serve"), Food("Steamed Rice", 1),Food("Beef Noodle Soup", 2, "Ready to Serve"))))



        val filteredOrders = mutableListOf <Serve>()
        for (i in orderList) {
            for (j in i.orders) {
                if (j.status == "Ready to Serve") {
                    filteredOrders.add(Serve(i.tableNo, j ))
                }
            }
        }


        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutPretty: String = gsonPretty.toJson(filteredOrders)
        println(jsonTutPretty)
        println(filteredOrders.size)


        val serveReadyView = t.findViewById<RecyclerView>(R.id.serveReadyView)
        serveReadyView.layoutManager = LinearLayoutManager(context)
        serveReadyView.adapter = ServeAdapter(filteredOrders)


        return t
    }

    inner class ServeHolder(view: View): RecyclerView.ViewHolder(view){
        val serveTable = itemView.serveTable
        val serveMenu = itemView.serveMenu
        val serveQty = itemView.serveQty
        val serveAct = itemView.serveAct
    }
    inner class ServeAdapter (var filteredOrders: List<Serve>): RecyclerView.Adapter<ServeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServeHolder {
            val view = layoutInflater.inflate(R.layout.row_serve, parent, false)
            return ServeHolder(view)
        }
        override fun onBindViewHolder(holder: ServeHolder, position: Int) {
            holder.serveTable.text = filteredOrders[position].tableNo.toString()
            holder.serveMenu.text = filteredOrders[position].food.name
            holder.serveQty.text = filteredOrders[position].food.quantity.toString()


//            holder.serveAct.setOnClickListener {
                // pop up dialog
//                val dialogBinding = layoutInflater.inflate(R.layout.dialog_history,null)
//
//                val orderDialog = Dialog(requireContext())
//
//                val orderDialogTitle = dialogBinding.findViewById<TextView>(R.id.orderDialogTitle)
//                orderDialogTitle.text = tableText
//
//                orderDialog.setContentView(dialogBinding)
//                orderDialog.setCancelable(true)
//                orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                orderDialog.show()
//            }

        }
        override fun getItemCount(): Int {
            return filteredOrders.size
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}