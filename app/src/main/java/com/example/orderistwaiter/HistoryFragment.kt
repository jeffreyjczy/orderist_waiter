package com.example.orderistwaiter

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.row_history.*
import kotlinx.android.synthetic.main.row_history.view.*
import kotlinx.android.synthetic.main.row_history_dialog.view.*
import kotlinx.android.synthetic.main.row_order.view.*

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val t = inflater.inflate(R.layout.fragment_history, container, false)

        // History table
        val orderList = mutableListOf <Orders>()
        orderList.add(Orders(1, arrayListOf(Food("Chicken Fried Rice", 1))))
        orderList.add(Orders(2, arrayListOf(Food("Beef Noodle Soup", 2), Food("Egg Fried Rice", 1))))
        orderList.add(Orders(3, arrayListOf(Food("Vegetable Stir Fry", 3), Food("Steamed Rice", 1),Food("Beef Noodle Soup", 2))))

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonTutPretty: String = gsonPretty.toJson(orderList)
        println(jsonTutPretty)
        println(orderList.size)


        val historyView = t.findViewById<RecyclerView>(R.id.historyView)
        historyView.layoutManager = LinearLayoutManager(context)
        historyView.adapter = HistoryAdapter(orderList)



        return t
    }

    inner class HistoryHolder(view: View): RecyclerView.ViewHolder(view){
        val historyText = itemView.historyText
        val historyTableNo = itemView.historyRow
    }
    inner class HistoryAdapter (var orderList: List<Orders>): RecyclerView.Adapter<HistoryFragment.HistoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryFragment.HistoryHolder {
            val view = layoutInflater.inflate(R.layout.row_history, parent, false)
            return HistoryHolder(view)
        }
        override fun onBindViewHolder(holder: HistoryFragment.HistoryHolder, position: Int) {
            holder.historyText.text = "Order Table " + orderList[position].tableNo.toString()

            holder.historyTableNo.setOnClickListener {

                // pop up dialog

                val dialogBinding = layoutInflater.inflate(R.layout.dialog_history,null)

                // hitory dialog List View
                val historyViewDialog = dialogBinding.findViewById<RecyclerView>(R.id.historyDialogView)

                val tableOrder = orderList[position].orders
                historyViewDialog.layoutManager = LinearLayoutManager(context)
                historyViewDialog.adapter = HistoryDialogAdapter(tableOrder)
//
                val orderDialog = Dialog(requireContext())
//
//                val orderDialogTitle = dialogBinding.findViewById<TextView>(R.id.orderDialogTitle)
//                orderDialogTitle.text = tableText
//
                orderDialog.setContentView(dialogBinding)
                orderDialog.setCancelable(true)
                orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                orderDialog.show()
            }

        }
        override fun getItemCount(): Int {
            return orderList.size
        }
    }


    // History Dialog Recycler View Functions
    inner class HistoryDialogHolder(view: View): RecyclerView.ViewHolder(view){
        val historyDialogMenu = itemView.historyDialogMenu
        val historyDialogQuantity = itemView.historyDialogQuantity
        var historyDialogStatus = itemView.historyDialogStatus

    }
    inner class HistoryDialogAdapter (var orderList: List<Food>): RecyclerView.Adapter<HistoryDialogHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDialogHolder {
            val view = layoutInflater.inflate(R.layout.row_history_dialog, parent, false)
            return HistoryDialogHolder(view)
        }
        override fun onBindViewHolder(holder: HistoryDialogHolder, position: Int) {

            holder.historyDialogMenu.text   = orderList[position].name
            holder.historyDialogQuantity.text   = orderList[position].quantity.toString()
            holder.historyDialogStatus.text   =  orderList[position].status


        }
        override fun getItemCount(): Int {
            return orderList.size
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}