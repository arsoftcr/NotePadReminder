package com.mobile.notepadreminder.navigation

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobile.notepadreminder.pages.*

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SetupNavGraph(navController: NavHostController) {
    val backHandler=true
    val context=LocalContext.current
    val msj="El boton de Android esta deshabilitado"
    NavHost(navController = navController,
        startDestination = Screen.main.route){

        composable(route= Screen.main.route){
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            MainPage(navController)
        }
        composable(
            route = "add/{param}",
            arguments = listOf(navArgument("param") { type = NavType.IntType })
        ) { backStackEntry ->
            val intValue = backStackEntry.arguments?.getInt("param")
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            AddPage(navController, intValue)
        }
        composable(route= Screen.lis.route){
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            ListPage(navController)
        }
        composable(route= Screen.completed.route){
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            CompleteTaskPage(navController)
        }
        composable(route= Screen.encurso.route){
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            EnCursoTaskPage(navController)
        }
        composable(route= Screen.deHoy.route){
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            DeHoyPage(navController)
        }
        composable(route= Screen.total.route){
            BackHandler(backHandler) {
                Toast.makeText(context,"$msj",Toast.LENGTH_SHORT).show()
            }
            TotalPage(navController)
        }

    }
}