package com.apps.fatty.data

import com.apps.fatty.data.model.CourseDataModel
import com.apps.fatty.data.repo.InvalidYearRange
import com.apps.fatty.data.repo.OfflineFirstCoursesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

//quite a straightforward test, just to provide an example
//within other modules we should have tests as well,
// especially the most interesting part would be domain and feature/ui - with espresso
class CoursesRepoTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var repo: OfflineFirstCoursesRepository


    @Before
    fun setup() {
        repo = OfflineFirstCoursesRepository(
            FakeRemoteDataSource(getDummyCourses()),
            FakeLocalDataSource()
        )
    }

    fun getDummyCourses(): List<CourseDataModel> {
        //you can go with any preferable way to have synthetic data:
        // use MockWebServer
        // use a static json file
        // have manually created models
        return listOf(
            CourseDataModel("1231321", "Physics", "One year course"),
            CourseDataModel("1232321", "Math", "Two year course"),
            CourseDataModel("1233321", "Art and design", "Three year course"),
            CourseDataModel("1234321", "Finance", "Four year course"),
            CourseDataModel("1235321", "Philosophy", "Lifetime course"),
        )
    }

    @Test
    fun `verify data consistency`() {
        val originalDataSet = getDummyCourses().map { it.toDomainModel() }
        testScope.runTest {
            val result = repo.getCoursesForYear(1994, 2, 100).first()
            assertEquals(originalDataSet, result)

        }
    }

    @Test(expected = InvalidYearRange::class)
    fun `verify invalid year validation`() {
        testScope.runTest {
            repo.getCoursesForYear(1993, 2, 100).first()
        }
    }
}