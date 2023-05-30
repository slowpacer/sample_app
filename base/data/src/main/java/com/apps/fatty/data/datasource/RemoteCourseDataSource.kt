package com.apps.fatty.data.datasource

import com.apps.fatty.data.model.CourseDataModel
import kotlinx.coroutines.flow.Flow

//TODO: have a single declaration of data source and priority mechanism for data retrieval amd sync within diff sources
//TODO: i.e. val dataSources = sortedMapOf<Int, CrmInstanceRepo>(
//            CACHED_SOURCE to cachedCourses,
//            LOCAL_SOURCE to databaseCourses,
//            REMOTE_SOURCE to remoteCourses,
//        )
//TODO: sync them after retrieval, so basically downwards and upwards mechanism based on the desired strategy
//TODO: retrieval strategy should be dictated by an upper level(repo)
interface RemoteCourseDataSource {
    suspend fun findCoursesForYear(year: Int, page: Int, perPage: Int): List<CourseDataModel>
}

// mainly an abstraction for database, good example -> implementation should be withing the room database module
interface LocalCourseDataSource {
    suspend fun saveCourses(courses: List<CourseDataModel>):Flow<List<CourseDataModel>>
    fun findCoursesForYear(year: Int, page: Int, perPage: Int): Flow<List<CourseDataModel>>
}