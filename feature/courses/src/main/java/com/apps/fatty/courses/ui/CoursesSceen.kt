package com.apps.fatty.courses.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.apps.fatty.courses.CoursesViewModel
import com.apps.fatty.courses.ui.model.CourseUIModel
import com.apps.fatty.domain.model.CourseDomainModel

private const val TAG = "Courses"

//TODO: integrate within the navigation package - NavGraphBuilder etc.
@Composable
fun CoursesRoute(
    onCourseClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    coursesViewModel: CoursesViewModel
    /** = use injection framework **/

) {
    // might be additionally wrapped with the "Result" approach
    val courses: LazyPagingItems<CourseUIModel> =
        coursesViewModel.pagingData.collectAsLazyPagingItems()
    CoursesScreen(uiState = courses, onCourseClick)

}

@Composable
internal fun CoursesScreen(
    uiState: LazyPagingItems<CourseUIModel>,
    onCourseClick: (String) -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(uiState) { item ->
            item?.let { wrapped ->
                CourseItem(onClick = {
                    onCourseClick(it.id)
                    Log.v(TAG, "Course was clicked =${it.name}")
                }, course = wrapped)
            }
        }
    }
}

@Composable
fun CourseItem(onClick: (CourseUIModel) -> Unit, course: CourseUIModel) {
    Card(
        modifier = Modifier
            .padding(
                bottom = 5.dp, top = 5.dp,
                start = 5.dp, end = 5.dp
            )
            .fillMaxWidth()
            .clickable(onClick = { onClick(course) }),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Surface(
                modifier = Modifier.size(130.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.2f
                )
            ) {

                val image =
                    rememberAsyncImagePainter(course.image)
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp)
                        .clip(shape = RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = course.name,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontSize = 22.sp),
                    color = Color.Black
                )

                CompositionLocalProvider(
                    LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = course.about,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 25.dp)
                    )
                }
            }
        }
    }
}