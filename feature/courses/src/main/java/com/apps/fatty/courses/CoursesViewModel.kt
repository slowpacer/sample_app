package com.apps.fatty.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import coil.network.HttpException
import com.apps.fatty.courses.ui.model.CourseUIModel
import com.apps.fatty.data.datasource.LocalCourseDataSource
import com.apps.fatty.data.datasource.RemoteCourseDataSource
import com.apps.fatty.data.repo.OfflineFirstCoursesRepository
import com.apps.fatty.database.datasource.FakeCourseDataSource
import com.apps.fatty.domain.base.UseCase
import com.apps.fatty.domain.model.CourseDomainModel
import com.apps.fatty.domain.repo.CoursesRepository
import com.apps.fatty.domain.usecase.CoursesInquiry
import com.apps.fatty.domain.usecase.GetCoursesUseCase
import com.apps.fatty.network.datasource.CoursesRemoteDataSource
import com.apps.fatty.network.ktor.TeachersApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.IOException

//please note that the Presentation-layer was omitted
// in a "perfectly" grained architecture we might want to add that as well
class CoursesViewModel(
    private val remoteCoursesDataSource: RemoteCourseDataSource = CoursesRemoteDataSource(
        TeachersApi
    ),
    private val localCoursesDataSource: LocalCourseDataSource = FakeCourseDataSource(),
    private val coursesRepository: CoursesRepository =
        OfflineFirstCoursesRepository(
            remoteCoursesDataSource,
            localCoursesDataSource
        ),
    private val useCase: UseCase<CoursesInquiry, List<CourseDomainModel>> = GetCoursesUseCase(
        coursesRepository
    ),
    // wanna jump deeper -> generalize th view model
    private val coursesSource: CoursesSource<GetCoursesUseCase> = CoursesSource(useCase)
) : ViewModel() {

    private val _coursesFlow = MutableStateFlow<Result<List<CourseUIModel>>>(
        Result.Loading
    )

    // left in order to illustrate of manual handling of the paging approach
    val coursesFlow: StateFlow<Result<List<CourseUIModel>>>
        get() = _coursesFlow
    private var page = 1

    val pagingData: Flow<PagingData<CourseUIModel>> =
        Pager(PagingConfig(pageSize = 6)) { coursesSource }.flow.cachedIn(viewModelScope)


    fun fetchCourses() = viewModelScope.launch(Dispatchers.IO) {
        // example for mixed approaches within suspend <-> flow
        // hello mapping endless cycle
        // checkout this one - https://florentblot.medium.com/redundant-dto-domain-mapping-in-kotlin-flow-bffbd1d28fc8
        // or just gave away your "perfect" clean arch and keep it simple
        viewModelScope.launch(Dispatchers.IO) {
            useCase.flowExample(CoursesInquiry(2023, page++))
                .map {
                    it.map { dataCourse ->
                        CourseUIModel(dataCourse)
                    }
                }.asResult().collect {
                    _coursesFlow.tryEmit(it)
                }
        }
    }
}

//TODO: move to a common/generic place so that it can be used by others
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}