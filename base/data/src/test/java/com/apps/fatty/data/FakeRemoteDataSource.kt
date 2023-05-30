package com.apps.fatty.data

import com.apps.fatty.data.datasource.LocalCourseDataSource
import com.apps.fatty.data.datasource.RemoteCourseDataSource
import com.apps.fatty.data.model.CourseDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
// you can go with mocking approach instead of implementing the fake/test classes
class FakeRemoteDataSource(private val dummyData: List<CourseDataModel>) : RemoteCourseDataSource {
    override suspend fun findCoursesForYear(
        year: Int,
        page: Int,
        perPage: Int
    ): List<CourseDataModel> {
        return dummyData
    }
}


//Note: the actual data source logic should be tested separately
class FakeLocalDataSource : LocalCourseDataSource {
    var localData = emptyList<CourseDataModel>()
    override fun findCoursesForYear(
        year: Int,
        page: Int,
        perPage: Int
    ): Flow<List<CourseDataModel>> {
        return flow {
            emit(localData)
        }
    }

    override suspend fun saveCourses(courses: List<CourseDataModel>): Flow<List<CourseDataModel>> {
        localData = courses
        return flow {
            emit(localData)
        }
    }
}