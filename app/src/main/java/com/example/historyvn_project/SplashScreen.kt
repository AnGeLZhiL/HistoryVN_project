package com.example.historyvn_project

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.historyvn_project.common.Global
import com.example.historyvn_project.databinding.ActivitySplashScreenBinding
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private var client = OkHttpClient()
    private lateinit var alertDialog: AlertDialog.Builder
    var activity = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        println("---------${token}")

        if (token == null){
            Handler().postDelayed({
                startActivity(Intent(this@SplashScreen, SignInActivity::class.java))
                finish()
            }, 1000)
        } else {
            alertDialog = AlertDialog.Builder(this)
            val request = Request.Builder()
                .addHeader(
                    "Authorization",
                    "Bearer ${token}"
                )
                .url("${Global.base_url}/user-token")
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
                    if (response.code == 200){
                        val json = JSONObject(response.body.string())
                        if (json.getBoolean("status")){
                            Global.role_id = json.getInt("role_id")
                            Handler(Looper.getMainLooper()).post {
                                Handler().postDelayed({
                                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                                    finish()
                                }, 1000)
                            }
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Handler().postDelayed({
                                startActivity(Intent(this@SplashScreen, SignInActivity::class.java))
                                finish()
                            }, 1000)
                        }
                    }
                }

            })
        }
    }
}