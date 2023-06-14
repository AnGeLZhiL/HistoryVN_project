package com.example.historyvn_project

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentStartTestBinding
import com.example.historyvn_project.databinding.FragmentTestStartBinding
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class StartTestFragment : Fragment() {

    private lateinit var binding: FragmentStartTestBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private var countQuestions = 0
    private var countCorrectAnswer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/questions/${Global.selectTest}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    alertDialog
                        .setTitle("Ошибка подключения")
                        .setMessage("Проверьте подключение к интернету или попробуйте повторить ошибку позже")
                        .setCancelable(true)
                        .setPositiveButton("Ok") { dialog, it ->
                            dialog.cancel()
                        }.show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200) {
                    val json = JSONArray(response.body.string())
                    println("---------------------- local count = ${countQuestions}")
                    println("---------------------- global count = ${Global.countQuestionsTest}")
                    if(countQuestions < Global.countQuestionsTest){
                        var jsonObject = json.getJSONObject(0)
                        println("----------------${jsonObject}")
                        Handler(Looper.getMainLooper()).post {
                            binding.questionText.text = jsonObject.getString("text_question")
                            binding.radioOne.text = jsonObject.getJSONArray("answers").getJSONObject(0).getString("text_answer")
                            binding.radioTwo.text = jsonObject.getJSONArray("answers").getJSONObject(1).getString("text_answer")
                            binding.radioThree.text = jsonObject.getJSONArray("answers").getJSONObject(2).getString("text_answer")
                            var count = countQuestions + 1
                            binding.questionCount.text = count.toString()
                            binding.buttonTest.setOnClickListener {
                                if (binding.radioOne.isChecked){
                                    if(jsonObject.getJSONArray("answers").getJSONObject(0).getBoolean("correctness")){
                                        countCorrectAnswer++
                                        countQuestions++
                                    } else {
                                        countQuestions++
                                    }
                                } else {
                                    if (binding.radioTwo.isChecked){
                                        if(jsonObject.getJSONArray("answers").getJSONObject(1).getBoolean("correctness")){
                                            countCorrectAnswer++
                                            countQuestions++
                                        } else {
                                            countQuestions++
                                        }
                                    } else {
                                        if (binding.radioThree.isChecked){
                                            if(jsonObject.getJSONArray("answers").getJSONObject(2).getBoolean("correctness")){
                                                countCorrectAnswer++
                                                countQuestions++
                                            } else {
                                                countQuestions++
                                            }
                                        }
                                    }
                                }
                                println("----------------${countCorrectAnswer}")
                                println("----------------${Global.countQuestionsTest}")
                                if(countQuestions < Global.countQuestionsTest) {
                                    binding.radioGroup.clearCheck()
                                    jsonObject = json.getJSONObject(countQuestions)
                                    binding.questionText.text = jsonObject.getString("text_question")
                                    binding.radioOne.text = jsonObject.getJSONArray("answers").getJSONObject(0).getString("text_answer")
                                    binding.radioTwo.text = jsonObject.getJSONArray("answers").getJSONObject(1).getString("text_answer")
                                    binding.radioThree.text = jsonObject.getJSONArray("answers").getJSONObject(2).getString("text_answer")
                                    count = countQuestions + 1
                                    binding.questionCount.text = count.toString()
                                } else {
                                    findNavController().navigate(R.id.action_startTestFragment_to_resultTestFragment)
                                    Global.countCorrectAnswer = countCorrectAnswer
                                    println("----------------${countCorrectAnswer}")
                                }
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            findNavController().navigate(R.id.action_startTestFragment_to_testsListFragment)
                        }
                    }
                }
            }
        })
    }
}