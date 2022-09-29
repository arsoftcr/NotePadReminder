package com.mobile.notepadreminder.navigation

sealed class Screen (val route:String){
    object main:Screen(route = "main")
    object add:Screen(route = "add")
    object lis:Screen(route = "lis")
    object completed:Screen(route = "completed")
    object encurso:Screen(route = "encurso")
    object deHoy:Screen(route = "deHoy")
    object total:Screen(route = "total")
}