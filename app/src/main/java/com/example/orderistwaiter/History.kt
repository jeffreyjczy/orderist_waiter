package com.example.orderistwaiter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.orderistwaiter.databinding.FragmentHistoryBinding
import kotlinx.android.synthetic.main.fragment_history.*

class History : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyTestBtn.setOnClickListener {
            val nextAction = HistoryDirections.nextAction()

            Navigation.findNavController(it).navigate(nextAction)
//            Navigation.findNavController(it).navigate(R.id.destination_historyTable)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
    }


}