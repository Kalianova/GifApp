package ru.kalianova.gifapp

import com.google.gson.annotations.SerializedName

data class GifDataModel(val url: String){

}

data class GifDataResult(@SerializedName("data") val data: List<GifDataObject>)

data class GifDataObject (@SerializedName("images") val images: GifDataImage, val title: String, val username: String, val import_datetime: String)

data class GifDataImage (@SerializedName("original") val original: GifOriginal)

data class GifOriginal (
    val url: String
        )