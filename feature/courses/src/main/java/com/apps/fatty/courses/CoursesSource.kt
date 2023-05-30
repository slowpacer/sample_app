package com.apps.fatty.courses

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.apps.fatty.courses.ui.model.CourseUIModel
import com.apps.fatty.domain.base.UseCase
import com.apps.fatty.domain.model.CourseDomainModel
import com.apps.fatty.domain.usecase.CoursesInquiry
import com.apps.fatty.domain.usecase.GetCoursesUseCase
import java.io.IOException

class CoursesSource<in T : UseCase<CoursesInquiry, List<CourseDomainModel>>>(private val useCase: T) :
    PagingSource<Int, CourseUIModel>() {

    override fun getRefreshKey(state: PagingState<Int, CourseUIModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CourseUIModel> {
        return try {
            val nextPage = params.key ?: 1
            val userList =
                useCase
                    .invoke(
                        CoursesInquiry(2023, nextPage)
                    ).map { CourseUIModel(it) }
            LoadResult.Page(
                data = userList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (userList.isEmpty()) null else nextPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}