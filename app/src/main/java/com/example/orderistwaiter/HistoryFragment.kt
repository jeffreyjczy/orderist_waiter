package com.example.orderistwaiter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.history_row.*

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val t = inflater.inflate(R.layout.fragment_order, container, false)

        // History table
//        val orderList = listOf(
////            Orders(1, ArrayList<Food>(Food(Menu("Chicken Fried Rice"), 1),)),
//        )

        val menuView = t.findViewById<RecyclerView>(R.id.orderView)
        menuView.layoutManager = LinearLayoutManager(context)
//        menuView.adapter = MenuAdapter(menuList, qtyList)



        return t
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyRow.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.destination_historyTable)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}