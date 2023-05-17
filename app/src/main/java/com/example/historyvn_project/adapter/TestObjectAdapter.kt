package com.example.historyvn_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyvn_project.R
import com.example.historyvn_project.databinding.TestObjectItemBinding
import com.example.historyvn_project.model.TestObjectModel

class TestObjectAdapter(val testObjectList: ArrayList<TestObjectModel>, val listner: TestObjectAdapter.Listner)
    : RecyclerView.Adapter<TestObjectAdapter.TestObjectViewHolder>() {

    interface Listner {
        fun onClickTestObject (testObjectModel: TestObjectModel)
    }

    class TestObjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TestObjectItemBinding.bind(itemView)

        fun bind(testObjectModel: TestObjectModel, listner: Listner) = with(binding) {
            nameTextView.text = testObjectModel.name

            itemView.setOnClickListener {
                listner.onClickTestObject(testObjectModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestObjectViewHolder {
        return TestObjectAdapter.TestObjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.test_object_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TestObjectViewHolder, position: Int) {
        holder.bind(testObjectList[position], listner)
    }

    override fun getItemCount(): Int = testObjectList.size
}