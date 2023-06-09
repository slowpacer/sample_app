package com.apps.fatty.grasper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.apps.fatty.courses.CoursesViewModel
import com.apps.fatty.courses.ui.CoursesRoute
import com.apps.fatty.grasper.ui.theme.GrasperTheme

class HomeActivity : ComponentActivity() {


    private val coursesViewModel: CoursesViewModel by viewModels {
        object : Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return CoursesViewModel(// ideally di will do the injection
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GrasperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // having an app state as a starting point should be better
                    CoursesRoute(onCourseClick = {
                        //TODO: your call
                    }, coursesViewModel = coursesViewModel)
                }
            }
        }
    }
}
