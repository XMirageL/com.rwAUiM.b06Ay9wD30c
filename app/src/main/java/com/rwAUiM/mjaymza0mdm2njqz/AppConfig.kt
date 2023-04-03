package com.rwAUiM.mjaymza0mdm2njqz
import com.google.gson.annotations.SerializedName


/**
 * Create by LinJi
 * 2023-03-21 17:59
 */
data class AppConfig(
    @SerializedName("data")
    val configData: ConfigData,
    @SerializedName("success")
    val success: Boolean
)

data class ConfigData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_update")
    val isUpdate: Int,
    @SerializedName("is_web")
    val isWeb: Int,
    @SerializedName("package_name")
    val packageName: String,
    @SerializedName("update_url")
    val updateUrl: String,
    @SerializedName("web_url")
    val webUrl: String
)