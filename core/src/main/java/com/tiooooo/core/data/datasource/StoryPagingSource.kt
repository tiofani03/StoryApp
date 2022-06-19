package com.tiooooo.core.data.datasource

// Niatnya mau diimplement, tapi dari BE ga provide jumlah page yang tersedia :(

//class StoryPagingSource(
//    private val service: StoriesService,
//    private val page: Int,
//    private val size: Int? = 10
//) : PagingSource<Int, StoriesViewParam>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesViewParam> {
//        return try {
//            val nextNumber = params.key ?: 1
//            val res = service.getStories(page,size)
//
//
//        }catch (e: Exception){
//            Timber.e(e.getErrorMessage())
//            LoadResult.Error(e)
//        }catch (e: HttpException){
//            Timber.e(e.getErrorMessage())
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, StoriesViewParam>): Int? {
//        TODO("Not yet implemented")
//    }
//}