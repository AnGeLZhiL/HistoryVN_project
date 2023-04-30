package com.example.historyvn_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.ActivitySignUpBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val emailRegex = "[a-z0-9]+@[a-z0-9]+\\.+[a-z]{2,3}"
    private val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#\$%^&*_+=.?]).{8,}\$"
    private val client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alertDialog = AlertDialog.Builder(this)

        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.signUpButton.setOnClickListener {
            if (binding.firstNameEditText.text.isNotEmpty() and binding.lastNameEditText.text.isNotEmpty() and
                    binding.middleNameEditText.text.isNotEmpty() and binding.loginEditText.text.isNotEmpty() and
                    binding.passEditText.text.isNotEmpty() and binding.passConfirmEditText.text.isNotEmpty() and
                    binding.loginEditText.text.toString().trim().matches(emailRegex.toRegex()) and
                    (binding.passEditText.text.toString() == binding.passConfirmEditText.text.toString()) and
                    binding.passEditText.text.toString().trim().matches(passwordRegex.toRegex())) {

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    JSONObject()
                        .put("last_name", binding.lastNameEditText.text.toString())
                        .put("first_name", binding.firstNameEditText.text.toString())
                        .put("midlle_name", binding.middleNameEditText.text.toString())
                        .put("login", binding.loginEditText.text.toString())
                        .put("password", BCrypt.withDefaults().hashToString(
                            12, binding.passEditText.text.toString().trim().toCharArray()))
                        .toString()
                )

                val request = Request.Builder()
                    .url("${Global.base_url}/register")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        this@SignUpActivity.runOnUiThread(java.lang.Runnable {
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
                        if (response.code == 201){
                            this@SignUpActivity.runOnUiThread(java.lang.Runnable {
                                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                            })
                        }
//                        this@SignUpActivity.runOnUiThread(java.lang.Runnable {
//                            alertDialog
//                                .setTitle("Ошибка подключения")
//                                .setMessage("${response.code}")
//                                .setCancelable(true)
//                                .setPositiveButton("Ok") { dialog, it ->
//                                    dialog.cancel()
//                                }.show()
//                        })
                    }

                })

            } else {
                if (binding.firstNameEditText.text.isEmpty()) binding.firstNameEditText.error = "Пустое поле"
                if (binding.lastNameEditText.text.isEmpty()) binding.lastNameEditText.error = "Пустое поле"
                if (binding.middleNameEditText.text.isEmpty()) binding.middleNameEditText.error = "Пустое поле"
                if (binding.loginEditText.text.isEmpty()) binding.loginEditText.error = "Пустое поле"
                else
                    if (!(binding.loginEditText.text.toString().trim().matches(emailRegex.toRegex())))
                        binding.loginEditText.error = "Некоректное заполнение"
                if (binding.passEditText.text.isEmpty()) binding.passEditText.error = "Пустое поле"
                else
                    if (!(binding.passEditText.text.toString().trim().matches(passwordRegex.toRegex())))
                        binding.passEditText.error = "Пароль должен содержать не менее 8 символов " +
                                "(обязательно рдну заглавну и прописную букву, один специальный символ, одну цифрц)"
                if (binding.passConfirmEditText.text.isEmpty()) binding.passConfirmEditText.error = "Пустое поле"
                if (binding.passEditText.text.toString() != binding.passConfirmEditText.text.toString())
                    binding.passConfirmEditText.error = "Пароли не совпадают"
            }
        }
    }
}