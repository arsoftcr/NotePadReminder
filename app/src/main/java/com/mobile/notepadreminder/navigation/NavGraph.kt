package com.mobile.notepadreminder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobile.notepadreminder.pages.AddPage
import com.mobile.notepadreminder.pages.ListPage
import com.mobile.notepadreminder.pages.MainPage

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
        /*composable(route= Screen.home.route){
            HomePage(windowSize = windowSize,navController)
        }
        composable(route= Screen.menu.route, arguments = listOf(navArgument("id"){
            type= NavType.LongType
        })){
            MenuPage(windowSize = windowSize,navController,it.arguments!!.getLong("id"))
        }
        composable(route= Screen.categories.route, arguments = listOf(navArgument("menu"){
            type= NavType.LongType
        })){
            CategoryPage(windowSize = windowSize,navController,it.arguments!!.getLong("menu"))
        }
        composable(route= Screen.subcategories.route, arguments = listOf(navArgument("menu"){
            type= NavType.LongType
        }, navArgument("category"){
            type= NavType.LongType
        })){
            SubCategoryPage(windowSize = windowSize,navController,
                it.arguments!!.getLong("menu"),it.arguments!!.getLong("category"))
        }
        composable(route= Screen.components.route, arguments = listOf(navArgument("menu"){
            type= NavType.LongType
        }, navArgument("subcategory"){
            type= NavType.LongType
        })){
            ComponentPage(windowSize = windowSize,navController,
                it.arguments!!.getLong("menu"),it.arguments!!.getLong("subcategory"))
        }*/
    }
}