package com.example.historyvn_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.historyvn_project.adapter.TestUserAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentProfileBinding
import com.example.historyvn_project.model.TestUserModel
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var client1 = OkHttpClient()
    private var client2 = OkHttpClient()
    private var client3 = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var testUserList: ArrayList<TestUserModel>
    private lateinit var testUserAdapter: TestUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            val sharedPreferences = this.requireActivity()
                .getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)

        println("----------${token}")

        alertDialog = AlertDialog.Builder(requireActivity())

        val request = Request.Builder()
            .addHeader(
                "Authorization",
                "Bearer ${token}"
            )
            .url("${Global.base_url}/user")
            .build()

        val requestUserTests = Request.Builder()
            .addHeader(
                "Authorization",
                "Bearer ${token}"
            )
            .url("${Global.base_url}/testsuser")
            .build()

        client1.newCall(request).enqueue(object : Callback {
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
                if (response.code == 200){
                    val json = JSONArray(response.body.string()).getJSONObject(0)
                    Handler(Looper.getMainLooper()).post{
                        binding.lastNameTextView.text = json.getString("last_name").toString()
                        binding.lmTextView.text = json.getString("first_name").toString() + " " + json.getString("midlle_name").toString()
                        binding.reitingTextView.text = json.getString("rating").toString()
                        binding.loginTextView.text = json.getString("login").toString()
                        println("----------------${Global.url_image+json.getString("image")}")
                        if (Global.selectImage == null && Global.updateImg == 0){
                            Picasso.get().load(Global.url_image+json.getString("image")).into(binding.userImg)
                        } else {
                            binding.userImg.setImageURI(Global.selectImage)
                        }
                    }
                }
            }

        })

        client2.newCall(requestUserTests).enqueue(object : Callback {
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
                if (response.code == 200){
                    val json = JSONArray(response.body.string())
                    testUserList = ArrayList()

                    println("------------${json}")

                    for (i in 0 until json.length()){
                        testUserList.add(
                            TestUserModel(
                                id = json.getJSONObject(i).getInt("id_test"),
                                name = json.getJSONObject(i).getString("name"),
                                mark = json.getJSONObject(i).getJSONObject("pivot").getString("mark").toFloat()
                            )
                        )
                    }
                    Handler(Looper.getMainLooper()).post{
                        testUserAdapter = TestUserAdapter(testUserList)
                        binding.collectionsRecyclerView.adapter = testUserAdapter
                    }
                }
            }

        })

        binding.testsVisibility.setOnClickListener {
            if (binding.collectionsRecyclerView.visibility == View.VISIBLE)
                binding.collectionsRecyclerView.visibility = View.GONE
            else
                binding.collectionsRecyclerView.visibility = View.VISIBLE
        }
        
        binding.logoutUser.setOnClickListener {
            Handler(Looper.getMainLooper()).post {
                val sharedPreferences = this.requireActivity()
                    .getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.apply{
                    putString("token", null)
                }.apply()
                startActivity(Intent(context, SignInActivity::class.java))
            }
        }

        binding.deleteUser.setOnClickListener {
            alertDialog
                .setTitle("Предупреждение")
                .setMessage("Вы уверены, что хотите удалить аккаунт?")
                .setCancelable(true)
                .setPositiveButton("Да") { dialog, it ->
                    val request3 = Request.Builder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${token}"
                        )
                        .url("${Global.base_url}/user-delete")
                        .build()
                    client3.newCall(request3).enqueue(object : Callback {
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
                            if (response.code == 200){
                                Handler(Looper.getMainLooper()).post{
                                    startActivity(Intent(context, SignUpActivity::class.java))
                                }
                            }
                        }

                    })
                }
                .setNegativeButton("Нет") { dialog, it ->
                    dialog.cancel()
                }.show()

            }
        binding.updateUser.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateUserFragment)
        }
    }
}