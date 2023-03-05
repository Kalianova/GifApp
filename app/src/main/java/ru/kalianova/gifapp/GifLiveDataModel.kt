package ru.kalianova.gifapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.annotations.SerializedName

class GifLiveDataModel constructor(
    private val gifsRepository: GifsDataSource
) : ViewModel() {
    val errorManager = MutableLiveData<String>()
    fun getGifsList(): LiveData<PagingData<GifDataObject>> {
        return gifsRepository.getGifs().cachedIn(viewModelScope)
    }
}

data class GifDataResult(@SerializedName("data") val data: List<GifDataObject>)

data class GifDataObject(
    @SerializedName("images") val images: GifDataImage,
    val title: String,
    val username: String,
    val import_datetime: String
)

data class GifDataImage(@SerializedName("original") val original: GifOriginal)

data class GifOriginal(
    val url: String
)