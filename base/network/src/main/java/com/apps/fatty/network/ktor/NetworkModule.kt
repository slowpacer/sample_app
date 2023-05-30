package com.apps.fatty.network.ktor

import androidx.core.net.toUri
import com.apps.fatty.grasper.network.model.NestedDataResponse
import com.apps.fatty.network.ktor.QueryParam.*
import com.apps.fatty.network.ktor.UrlPath.*
import com.apps.fatty.network.model.Course
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL =
    "https://api.publish-teacher-training-courses.service.gov.uk/api/public/v1/"
private const val PAGE_QUERY_PARAM_KEY = "page"
private const val PAGE_CONTAINER_QUERY_PARAM_KEY = "page"
private val client = HttpClient(Android) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    defaultRequest {
        accept(ContentType.Application.Json)
        url(BASE_URL)
    }
}
// decided to use Ktor as teh plan is to move it to KMM
object TeachersApi {
    // url example:
    // https://api.publish-teacher-training-courses.service.gov.uk/api/public/v1/recruitment_cycles/2023/courses?page[page]=2&&page[per_page]=10
    suspend fun fetchCourses(
        year: Int,
        page: Int,
        perPage: Int
    ): NestedDataResponse<Course> =
        client.get(
            BASE_URL
                .appendPath(Year(year), Courses())
                .appendQueryParams(
                    DeepObject(
                        PAGE_CONTAINER_QUERY_PARAM_KEY,
                        Page(page)
                    ),
                    DeepObject(
                        PAGE_CONTAINER_QUERY_PARAM_KEY,
                        PerPage(perPage)
                    )
                )
        ).body()
}

//TODO: come-up with a better url forming approach, any pattern to rescue?
internal sealed class UrlPath(val key: String, val value: String?) {
    /**
     * Could be used for simple cases in REST api.
     * i.e. baseUrl/courses/{course_id}
     */
    fun asSelfContained() = "/$key/$value"

    class Year(year: Int) : UrlPath("recruitment_cycles", "$year")
    class Courses(courseCode: String? = null) : UrlPath("courses", courseCode)

}

//TODO: come-up with a better url query params building approach, any pattern to rescue?
//TODO: extract strings to const values
internal sealed class QueryParam(val key: String, val value: String) {
    fun asSelfContained() = "?$key=$value"


    class Page(page: Int) : QueryParam(PAGE_QUERY_PARAM_KEY, "$page")
    class PerPage(amount: Int) : QueryParam("per_page", "$amount")
    class DeepObject<T : QueryParam>(queryContainer: String, queryParam: T) :
        QueryParam("$queryContainer[${queryParam.key}]", queryParam.value)
}

//TODO: Consider moving to strings, so that it could be covered with unit tests
internal fun String.appendPath(vararg params: UrlPath) = toUri().buildUpon().apply {
    params.forEach { path ->
        appendPath(path.key)
        path.value?.let { appendPath(it) }
    }
}.toString()

internal fun String.appendQueryParams(vararg params: QueryParam) = toUri().buildUpon().apply {
    params.forEach {
        this.appendQueryParameter(it.key, it.value)

    }
}.toString()
