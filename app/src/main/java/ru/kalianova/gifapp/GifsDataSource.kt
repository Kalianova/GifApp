package ru.kalianova.gifapp

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData

const val PAGE_SIZE = 25

class GifsDataSource constructor(private val service: GetDataService, var q: String) {
    fun getGifs(): LiveData<PagingData<GifDataObject>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE, enablePlaceholders = false
            ), pagingSourceFactory = {
                GifsPagingSource(gifService = service, q)
            }, initialKey = 0
        ).liveData
    }
}