package com.example.historyvn_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.ActivitySignInBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Global.pref = getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        alertDialog = AlertDialog.Builder(this)

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.signInButton.setOnClickListener {
            if (binding.loginEditText.text.isNotEmpty() and binding.passwordEditText.text.isNotEmpty()) {
                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    JSONObject()
                        .put("login", binding.loginEditText.text.toString())
                        .put("password", binding.passwordEditText.text.toString())
                        .toString()
                )

                val request = Request.Builder()
                    .url("${Global.base_url}/login")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        this@SignInActivity.runOnUiThread(java.lang.Runnable {
                            alertDialog
                                .setTitle("Ошибка подключения")
                                .setMessage("Проверьте подключение к интернету или попробуйте повторить ошибку позже")
                                .setCancelable(true)
                                .setPositiveButton("Ok") { dialog, it ->
                                    dialog.cancel()
                                }.show()
                        })
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.code == 200) {
                            val json =JSONObject(response.body.string())
                            val token = json.getJSONObject("user").getString("api_token")
                            saveToken(token)
                            this@SignInActivity.runOnUiThread(java.lang.Runnable {
                                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                            })
                        } else {
                            this@SignInActivity.runOnUiThread(java.lang.Runnable {
                                alertDialog
                                    .setTitle("Ошибка авторизации")
                                    .setMessage("Логин или пароль введен неверно")
                                    .setCancelable(true)
                                    .setPositiveButton("Ok") { dialog, it ->
                                        dialog.cancel()
                                    }.show()
                            })
                        }
                    }

                })
            } else {
                if (binding.loginEditText.text.isEmpty()) binding.loginEditText.error = "Пустое поле"
                if (binding.passwordEditText.text.isEmpty()) binding.passwordEditText.error = "Пустое поле"
            }
        }
    }

    fun saveToken(token: String){
        val editor = Global.pref?.edit()
        editor?.putString("token", token)
        editor?.apply()
    }

}