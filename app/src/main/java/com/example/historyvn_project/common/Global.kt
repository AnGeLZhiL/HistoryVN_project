package com.example.historyvn_project.common

import android.content.SharedPreferences

class Global {
    companion object {
        var base_url = "https://historyvn-api-production.up.railway.app/api"
        var pref: SharedPreferences? = null
    }
}