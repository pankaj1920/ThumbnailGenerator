package com.app.videoeditorlibrary.navigation


import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.videoeditorlibrary.navigation.routes.VideoLibRoutes
import com.app.videoeditorlibrary.ui.screen.HomeScreen
import com.app.videoeditorlibrary.ui.screen.ThumbnailGeneratorScreen
import com.app.videoeditorlibrary.ui.screen.VideoPickerScreen

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
@Composable
fun RootNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = VideoLibRoutes.VideoHomeScreen,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, // Consider the left direction for entering
                animationSpec = tween(1000)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, // Consider the left direction for exiting
                animationSpec = tween(1000)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(1000)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(1000)
            )
        },
    ) {
        composable<VideoLibRoutes.VideoHomeScreen> { // custom type as generic
            HomeScreen(navController)
        }
        composable<VideoLibRoutes.VideoPickerScreen> { // custom type as generic
            VideoPickerScreen(navController)
        }
        composable<VideoLibRoutes.VideoThumbnailScreen> { // custom type as generic
            ThumbnailGeneratorScreen(navController)
        }

    }

}
