package com.mobile.notepadreminder.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobile.notepadreminder.pages.*

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController,
        startDestination = Screen.main.route){

        composable(route= Screen.main.route){
            MainPage(navController)
        }
        composable(route= Screen.add.route){
            AddPage(navController)
        }
        composable(route= Screen.lis.route){
            ListPage(navController)
        }
        composable(route= Screen.completed.route){
            CompleteTaskPage(navController)
        }
        composable(route= Screen.encurso.route){
            EnCursoTaskPage(navController)
        }
        composable(route= Screen.deHoy.route){
            DeHoyPage(navController)
        }
        composable(route= Screen.total.route){
            TotalPage(navController)
        }

    }
}