package com.example.historyvn_project

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.historyvn_project.adapter.TestUserAdapter
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.FragmentProfileBinding
import com.example.historyvn_project.databinding.FragmentUpdateUserBinding
import com.example.historyvn_project.model.TestUserModel
import com.squareup.picasso.Picasso
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UpdateUserFragment : Fragment() {

    private lateinit var binding: FragmentUpdateUserBinding
    private var client1 = OkHttpClient()
    private var client2 = OkHttpClient()
    private var client3 = OkHttpClient()
    private var client4 = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    var auth = ""
    private val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#\$%^&*_+=.?]).{8,}\$"
    var firstName = ""
    var middleName = ""
    var lastName = ""
    var loginName = ""

    companion object {
        val IMAGE_REQEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentUpdateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = this.requireActivity()
            .getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        auth = token.toString()

        alertDialog = AlertDialog.Builder(requireActivity())

        binding.userImg.setOnClickListener{
            pickImageGallery()
        }

        val request = Request.Builder()
            .addHeader(
                "Authorization",
                "Bearer ${token}"
            )
            .url("${Global.base_url}/user")
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
                        binding.firstNameEditText.hint = json.getString("first_name").toString()
                        binding.lastNameEditText.hint = json.getString("last_name").toString()
                        binding.middleNameEditText.hint = json.getString("midlle_name").toString()
                        binding.loginEditText.hint = json.getString("login").toString()
//                        binding.lastNameTextView.text = json.getString("last_name").toString()
//                        binding.lmTextView.text = json.getString("first_name").toString() + " " + json.getString("midlle_name").toString()
//                        binding.reitingTextView.text = json.getString("rating").toString()
//                        binding.loginTextView.text = json.getString("login").toString()
//                        println("----------------${Global.url_image+json.getString("image")}")
//                        Picasso.get().load(Global.url_image+json.getString("image")).into(binding.userImg)
                    }
                }
            }

        })

        binding.updatePassword.setOnClickListener {
            if (binding.password.visibility == View.VISIBLE)
                binding.password.visibility = View.GONE
            else
                binding.password.visibility = View.VISIBLE
        }

        binding.updateUser.setOnClickListener {
            if (binding.password.visibility == View.VISIBLE) {
                if (binding.passwordEditText.text.isNotEmpty() && binding.passwordConfNameEditText.text.isNotEmpty()){
                    if (binding.passwordEditText.text.toString().trim().matches(passwordRegex.toRegex()))  {
                        if (binding.passwordEditText.text.toString() == binding.passwordConfNameEditText.text.toString()){
                            if (binding.lastNameEditText.text.isNotEmpty()){
                                lastName = binding.lastNameEditText.text.toString()
                            } else lastName = binding.lastNameEditText.hint.toString()

                            if (binding.firstNameEditText.text.isNotEmpty()){
                                firstName = binding.firstNameEditText.text.toString()
                            } else firstName = binding.firstNameEditText.hint.toString()

                            if (binding.middleNameEditText.text.isNotEmpty()){
                                middleName = binding.middleNameEditText.text.toString()
                            } else middleName = binding.middleNameEditText.hint.toString()

                            if (binding.loginEditText.text.isNotEmpty()){
                                loginName = binding.loginEditText.text.toString()
                            } else loginName = binding.loginEditText.hint.toString()

                            val requestBody2 = RequestBody.create(
                                "application/json".toMediaTypeOrNull(),
                                JSONObject()
                                    .put("last_name", lastName)
                                    .put("first_name", firstName)
                                    .put("midlle_name", middleName)
                                    .put("login", loginName)
                                    .put("birthday", null)
                                    .toString()
                            )

                            val request3 = Request.Builder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer ${token}"
                                )
                                .url("${Global.base_url}/user-update")
                                .put(requestBody2)
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
                                    if (response.code == 200) {
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(context, "Данные успешно изменены", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Handler(Looper.getMainLooper()).post {
                                            alertDialog
                                                .setTitle("${response.code}")
                                                .setMessage("Проверьте подключение к интернету или попробуйте повторить попытку позже")
                                                .setCancelable(true)
                                                .setPositiveButton("Ok") { dialog, it ->
                                                    dialog.cancel()
                                                }.show()
                                        }
                                    }
                                }
                            })
                            val requestBody3 = RequestBody.create(
                                "application/json".toMediaTypeOrNull(),
                                JSONObject()
                                    .put("password", BCrypt.withDefaults().hashToString(
                                        12, binding.passwordEditText.text.toString().trim().toCharArray()))
                                    .toString()
                            )

                            val request4 = Request.Builder()
                                .addHeader(
                                    "Authorization",
                                    "Bearer ${token}"
                                )
                                .url("${Global.base_url}/password-update")
                                .put(requestBody3)
                                .build()

                            client4.newCall(request4).enqueue(object : Callback {
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
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(context, "Пароль изменен", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Handler(Looper.getMainLooper()).post {
                                            alertDialog
                                                .setTitle("Ошибка подключения")
                                                .setMessage("Проверьте подключение к интернету или попробуйте повторить попытку позже")
                                                .setCancelable(true)
                                                .setPositiveButton("Ok") { dialog, it ->
                                                    dialog.cancel()
                                                }.show()
                                        }
                                    }
                                }
                            })
                        } else
                            binding.passwordConfNameEditText.error = "Пароли не совпадают"
                    } else {
                        binding.passwordEditText.error = "Пароль должен содержать не менее 8 символов " +
                                "(обязательно рдну заглавну и прописную букву, один специальный символ, одну цифрц)"
                    }
                } else {
                    if (binding.passwordEditText.text.isEmpty()) binding.passwordEditText.error = "Пустое поле"
                    if (binding.passwordConfNameEditText.text.isEmpty()) binding.passwordConfNameEditText.error = "Пустое поле"
                }
            } else {
                if (binding.lastNameEditText.text.isNotEmpty()){
                        lastName = binding.lastNameEditText.text.toString()
                } else lastName = binding.lastNameEditText.hint.toString()

                if (binding.firstNameEditText.text.isNotEmpty()){
                        firstName = binding.firstNameEditText.text.toString()
                    } else firstName = binding.firstNameEditText.hint.toString()

                if (binding.middleNameEditText.text.isNotEmpty()){
                        middleName = binding.middleNameEditText.text.toString()
                    } else middleName = binding.middleNameEditText.hint.toString()

                if (binding.loginEditText.text.isNotEmpty()){
                        loginName = binding.loginEditText.text.toString()
                    } else loginName = binding.loginEditText.hint.toString()

                println("---------------${loginName}")
                println("---------------${middleName}")

                val requestBody2 = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    JSONObject()
                        .put("last_name", lastName)
                        .put("first_name", firstName)
                        .put("midlle_name", middleName)
                        .put("login", loginName)
                        .put("birthday", null)
                        .toString()
                )

                val request3 = Request.Builder()
                    .addHeader(
                        "Authorization",
                        "Bearer ${token}"
                    )
                    .url("${Global.base_url}/user-update")
                    .put(requestBody2)
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
                        if (response.code == 200) {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(context, "Данные успешно изменены", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Handler(Looper.getMainLooper()).post {
                                alertDialog
                                    .setTitle("Ошибка подключения")
                                    .setMessage("Проверьте подключение к интернету или попробуйте повторить попытку позже")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok") { dialog, it ->
                                        dialog.cancel()
                                    }.show()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQEST_CODE && resultCode == RESULT_OK){
            binding.userImg.setImageURI(data?.data)
            println("-----------${data?.data}")

//            val requestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", data?.data)
//
//            val request2 = Request.Builder()
//                .addHeader(
//                    "Authorization",
//                    "Bearer ${auth}"
//                )
//                .url("${Global.base_url}/user-img-upload")
//                .post(requestBody)
//                .build()
//
//            client2.newCall(request2).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    Handler(Looper.getMainLooper()).post {
//                        alertDialog
//                            .setTitle("Ошибка подключения")
//                            .setMessage("Проверьте подключение к интернету или попробуйте повторить ошибку позже")
//                            .setCancelable(true)
//                            .setPositiveButton("Ok") { dialog, it ->
//                                dialog.cancel()
//                            }.show()
//                    }
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    if (response.code == 200){
//                        Handler(Looper.getMainLooper()).post{
//                            Toast.makeText(context, "Сделано", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Handler(Looper.getMainLooper()).post {
//                            Toast.makeText(context, "${response.code}", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//            })
//            println("-----------Hello")
        }
    }
}