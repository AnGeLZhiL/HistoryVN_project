package com.example.historyvn_project

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.models.SlideModel
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentTestStartBinding
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

class TestStartFragment : Fragment() {

    private lateinit var binding: FragmentTestStartBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private var countQuestions = 0
    private var countCorrectAnswer = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/questions/${Global.selectTestObject}")
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
                    if(countQuestions < Global.countQuestionsObjectTest){
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
                                println("----------------${Global.countQuestionsObjectTest}")
                                if(countQuestions < Global.countQuestionsObjectTest) {
                                    binding.radioGroup.clearCheck()
                                    jsonObject = json.getJSONObject(countQuestions)
                                    binding.questionText.text = jsonObject.getString("text_question")
                                    binding.radioOne.text = jsonObject.getJSONArray("answers").getJSONObject(0).getString("text_answer")
                                    binding.radioTwo.text = jsonObject.getJSONArray("answers").getJSONObject(1).getString("text_answer")
                                    binding.radioThree.text = jsonObject.getJSONArray("answers").getJSONObject(2).getString("text_answer")
                                    count = countQuestions + 1
                                    binding.questionCount.text = count.toString()
                                } else {
                                    findNavController().navigate(R.id.action_testStartFragment_to_resultTestObjectFragment)
                                    Global.countCorrectObjectAnswer = countCorrectAnswer
                                    println("----------------${countCorrectAnswer}")
                                }
                            }
                        }
                    } else {
//                        Handler(Looper.getMainLooper()).post {
//                            binding.radioGroup.visibility = View.GONE
//                            binding.buttonTest.visibility = View.GONE
//                            binding.questionText.visibility = View.GONE
//                            binding.questionCount.visibility = View.GONE
//                            binding.correct.visibility = View.VISIBLE
//                        }
                        Handler(Looper.getMainLooper()).post {
                            findNavController().navigate(R.id.action_testStartFragment_to_objectInformationFragment)
                        }
                    }
                }
            }
        })
        binding.back.setOnClickListener {
            alertDialog
                .setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите покинуть тест? Результат теста не будет засчитан")
                .setCancelable(true)
                .setPositiveButton("Да") { dialog, it ->
                    findNavController().navigate(R.id.action_testStartFragment_to_testFragment)
                }
                .setNegativeButton("Нет") { dialog, it ->
                    dialog.cancel()
                }.show()
        }
    }
}