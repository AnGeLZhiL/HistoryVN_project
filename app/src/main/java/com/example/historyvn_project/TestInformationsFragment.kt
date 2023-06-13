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
import com.example.historyvn_project.databinding.FragmentTestBinding
import com.example.historyvn_project.databinding.FragmentTestInformationsBinding
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class TestInformationsFragment : Fragment() {

    private lateinit var binding: FragmentTestInformationsBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestInformationsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .url("${Global.base_url}/test/${Global.selectTest}")
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
                    Global.countQuestionsTest = 0
                    val json = JSONArray(response.body.string()).getJSONObject(0)
                    for (i in 0 until json.getJSONArray("questions").length()){
                        Global.countQuestionsTest++
                    }
                    Handler(Looper.getMainLooper()).post {
                        binding.testName.text = json.getString("name")
                        binding.countQuestion.text = Global.countQuestionsTest.toString()
                    }
                    Global.testPro2 = 0
                }
            }

        })

        binding.testStart.setOnClickListener {
//            findNavController().navigate(R.id.action_testFragment_to_testStartFragment)
        }
    }
}