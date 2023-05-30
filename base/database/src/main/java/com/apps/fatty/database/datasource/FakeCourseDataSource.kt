package com.apps.fatty.database.datasource

import androidx.annotation.VisibleForTesting
import com.apps.fatty.data.datasource.LocalCourseDataSource
import com.apps.fatty.data.model.CourseDataModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeCourseDataSource : LocalCourseDataSource {

    // Be aware due to buffering it will take some additional memory
    // should be profiled
    private val _fakeCoursesData = MutableSharedFlow<List<CourseDataModel>>(
        replay = 2,
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @VisibleForTesting
    var fakeCoursesData = mutableListOf<CourseDataModel>()
        set(value) {
            field.addAll(value)
            _fakeCoursesData.tryEmit(field)
        }

    override suspend fun saveCourses(courses: List<CourseDataModel>): Flow<List<CourseDataModel>> {
        fakeCoursesData = courses.toMutableList()
        return _fakeCoursesData
    }

    override fun findCoursesForYear(
        year: Int,
        page: Int,
        perPage: Int
    ): Flow<List<CourseDataModel>> {
        //db work should happen/ room returns its own flow
        return _fakeCoursesData
    }
}