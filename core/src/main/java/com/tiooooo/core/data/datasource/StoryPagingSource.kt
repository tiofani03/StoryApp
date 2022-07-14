package com.tiooooo.core.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tiooooo.core.data.remote.network.StoriesService
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.network.NetworkUtils.getErrorMessage
import retrofit2.HttpException
import timber.log.Timber


class StoryPagingSource(
    private val service: StoriesService
) : PagingSource<Int, StoryViewParam>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryViewParam> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val res = service.getStories(position, params.loadSize)
            LoadResult.Page(
                data = res.listStory!!.map {
                    it.toClean()
                },
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (res.listStory.isNullOrEmpty()) null else position + 1
            )

        } catch (e: Exception) {
            Timber.e(e.getErrorMessage())
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e.getErrorMessage())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryViewParam>): Int? =
        state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}