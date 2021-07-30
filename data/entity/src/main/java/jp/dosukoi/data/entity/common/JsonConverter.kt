package jp.dosukoi.data.entity.common

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder


object JsonConverter {
    val gson: Gson = GsonBuilder().let {
        it.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        it.setLenient()
        it.create()
    }
}