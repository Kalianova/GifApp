package ru.kalianova.gifapp

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException


class GifsPagingSource(private val gifService: GetDataService, var q: String) :
    PagingSource<Int, GifDataObject>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifDataObject> {
        return try {
            val nextGifNumber = params.key ?: 0
            val response =
                if (q == "") gifService.getGifs(API_KEY, nextGifNumber) else gifService.searchGifs(
                    API_KEY, nextGifNumber, q
                )
            LoadResult.Page(
                data = response.body()!!.data,
                prevKey = if (nextGifNumber > 0) nextGifNumber - 25 else null,
                nextKey = if (nextGifNumber < 3000) nextGifNumber + 25 else null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GifDataObject>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(25)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(25)
        }
    }

}