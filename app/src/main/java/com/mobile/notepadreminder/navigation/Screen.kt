package com.mobile.notepadreminder.navigation

sealed class Screen (val route:String){
    object main:Screen(route = "main")
    object add:Screen(route = "add")
    object lis:Screen(route = "lis")
   /* object home:Screen(route = "home")
    object menu:Screen(route = "menu/{id}")
    object categories:Screen(route = "categories/{menu}")
    object subcategories:Screen(route = "subcategories/{menu}/{category}")
    object components:Screen(route = "components/{menu}/{subcategory}")*/
}