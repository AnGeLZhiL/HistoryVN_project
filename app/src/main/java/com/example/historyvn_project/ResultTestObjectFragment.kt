package com.example.historyvn_project

import android.R
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentResultTestObjectBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import org.json.JSONObject


class ResultTestObjectFragment : Fragment() {

    private lateinit var binding: FragmentResultTestObjectBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultTestObjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = this.requireActivity()
            .getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        binding.countQuestion.text = Global.countQuestionsObjectTest.toString()
        binding.countRightQuestion.text = Global.countCorrectObjectAnswer.toString()
        binding.mark.text = markResult(Global.countQuestionsObjectTest, Global.countCorrectObjectAnswer).toString()

        if (markResult(Global.countQuestionsObjectTest, Global.countCorrectObjectAnswer) == 2){
            binding.mark.setTextColor(Color.parseColor("#FA9A9A"))
        } else if (markResult(Global.countQuestionsObjectTest, Global.countCorrectObjectAnswer) == 3){
            binding.mark.setTextColor(Color.parseColor("#EDA57D"))
        } else if (markResult(Global.countQuestionsObjectTest, Global.countCorrectObjectAnswer) == 4){
            binding.mark.setTextColor(Color.parseColor("#EFF687"))
        } else
            binding.mark.setTextColor(Color.parseColor("#B1F68B"))

        alertDialog = AlertDialog.Builder(requireActivity())

        if (Global.testPro == 0) {

            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                JSONObject()
                    .put("test_id", Global.selectTestObject)
                    .put("passed", true)
                    .put(
                        "mark",
                        markResult(Global.countQuestionsObjectTest, Global.countCorrectObjectAnswer)
                    )
                    .toString()
            )

            val request = Request.Builder()
                .addHeader(
                    "Authorization",
                    "Bearer ${token}"
                )
                .url("${Global.base_url}/user-test")
                .post(requestBody)
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
                        println("-------------${response.code}")
                        Global.testPro = 1
                    } else {
                        println("-------------${response.code}")
                        Handler(Looper.getMainLooper()).post {
                            alertDialog
                                .setTitle("Ошибка подключения")
                                .setMessage("Попробуйте повторить попытку позже")
                                .setCancelable(true)
                                .setPositiveButton("Ok") { dialog, it ->
                                    dialog.cancel()
                                }.show()
                        }
                    }
                }

            })
        }

        binding.back.setOnClickListener {
            findNavController().navigate(com.example.historyvn_project.R.id.action_resultTestObjectFragment_to_objectInformationFragment3)
        }
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