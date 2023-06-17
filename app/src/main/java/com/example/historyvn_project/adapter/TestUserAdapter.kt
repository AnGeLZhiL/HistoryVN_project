package com.example.historyvn_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.historyvn_project.R
import com.example.historyvn_project.databinding.ActivityMainBinding.bind
import com.example.historyvn_project.databinding.TestObjectItemBinding
import com.example.historyvn_project.databinding.TestUserItemBinding
import com.example.historyvn_project.model.TestObjectModel
import com.example.historyvn_project.model.TestUserModel

class TestUserAdapter (val testUserList: ArrayList<TestUserModel>)
    : RecyclerView.Adapter<TestUserAdapter.TestUserViewHolder>() {

    class TestUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = TestUserItemBinding.bind(itemView)

        fun bind(testUserModel: TestUserModel) = with(binding) {
            nameTextView.text = testUserModel.name
            markTextView.text = testUserModel.mark.toString();
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestUserViewHolder {
        return TestUserAdapter.TestUserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.test_user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TestUserViewHolder, position: Int) {
        holder.bind(testUserList[position])
    }

    override fun getItemCount(): Int = testUserList.size
}