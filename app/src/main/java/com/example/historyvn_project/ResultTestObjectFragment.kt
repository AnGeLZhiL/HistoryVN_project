package com.example.historyvn_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentObjectInformationBinding
import com.example.historyvn_project.databinding.FragmentResultTestObjectBinding


class ResultTestObjectFragment : Fragment() {

    private lateinit var binding: FragmentResultTestObjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultTestObjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countQuestion.text = Global.countQuestionsObjectTest.toString()
        binding.countRightQuestion.text = Global.countCorrectAnswer.toString()
        binding.mark.text = markResult(Global.countQuestionsObjectTest, Global.countCorrectAnswer).toString()
    }

    fun markResult(countQuestion: Int, countRightQuestion: Int) : Int{
        val resultPircent = countRightQuestion * 100 / countQuestion
        println("---------------------${resultPircent}")
        if (resultPircent < 50) return 2
        else if ((50 <= resultPircent) and (resultPircent < 70)) return 3
        else if ((70 <= resultPircent) and (resultPircent < 90)) return 4
        else return 5
    }
}