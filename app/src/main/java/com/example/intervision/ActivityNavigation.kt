/**
 * Copyright Lectoraat Legal Management van de Hogeschool van Amsterdam
 *
 * Gemaakt door Nathan Misset 2024
 */

package com.example.intervision

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intervision.ui.IntervisionBaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 *
 * This activity controls navigation between the home groep and profile screen
 *
 */

@Suppress("DEPRECATION")
class ActivityNavigation : ComponentActivity() {

    /** Fragments */
    private var fragmentHome: FragmentHome? = null
    private var fragmentGroups: FragmentGroups? = null
    private var fragmentProfile: FragmentProfile? = null

    /** Firebase */
    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null
    private var auth: FirebaseAuth? = null

    sealed class Screen(val route: String) {
        data object Home : Screen("home")
        data object Group : Screen("group")
        data object Profile : Screen("profile")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Firebase */
        user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()



        /** Fragments */
        fragmentHome = FragmentHome()
        fragmentHome!!.init(user!!, db!!, this)
        fragmentGroups = FragmentGroups()
        fragmentGroups!!.init(auth!!,db!!,storage!!,this)
        fragmentProfile = FragmentProfile()
        fragmentProfile!!.init(user!!,storage!!,db!!,this)
        Log.d(TAG, "OnCreate")
    }

    override fun onRestart() {
        super.onRestart()
        super.onStart()
        Log.d(TAG, "OnStart")
        Handler().postDelayed({
            setContent {
                IntervisionBaseTheme {
                    Log.d("navigation", "setContent")
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomAppBarWithNavigation(navController = navController) },
                    ) { paddingValues ->
                        paddingValues

                        NavHost(navController, startDestination = Screen.Home.route) {
                            composable(Screen.Home.route) { HomeScreen() }
                            composable(Screen.Group.route) { GroupScreen() }
                            composable(Screen.Profile.route) { ProfileScreen() }
                        }
                    }
                }
            }
        }, 1500)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart")
        Handler().postDelayed({
            setContent {
                IntervisionBaseTheme {
                    Log.d("navigation", "setContent")
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomAppBarWithNavigation(navController = navController) },
                    ) { paddingValues ->
                        paddingValues

                        NavHost(navController, startDestination = Screen.Home.route) {
                            composable(Screen.Home.route) { HomeScreen() }
                            composable(Screen.Group.route) { GroupScreen() }
                            composable(Screen.Profile.route) { ProfileScreen() }
                        }
                    }
                }
            }
        }, 1500)
    }





        @Composable
        fun BottomAppBarWithNavigation(navController: NavHostController) {

            val items = listOf(
                BottomNavItem.Home,
                BottomNavItem.Group,
                BottomNavItem.Profile
            )

            NavigationBar {
                items.forEach { item ->
                    AddItem(
                        screen = item,
                        navController = navController
                    )
                }
            }
        }

        @Composable
        fun RowScope.AddItem(
            screen: BottomNavItem,
            navController: NavHostController
        ) {
            NavigationBarItem(
                label = {
                    Text(text = screen.title)
                },

                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.title
                    )
                },

                enabled = true,

                selected = false,

                alwaysShowLabel = true,

                onClick = {

                    navController.navigate(screen.screen.route)
                          },


                // Control all the colors of the icon
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )
    }


    sealed class BottomNavItem(
        var title: String,
        var icon: ImageVector,
        var screen: Screen
    ) {
        object Home :
            BottomNavItem(
                "Home",
                Icons.Default.Home,
                Screen.Home
            )

        object Group :
            BottomNavItem(
                "Groep",
                Icons.Default.Group,
                Screen.Group
            )


        object Profile :
            BottomNavItem(
                "Profile",
                Icons.Default.AccountCircle,
                Screen.Profile
            )
    }


    @Composable
    fun HomeScreen() {
            IntervisionBaseTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    fragmentHome!!.Component()
                }
            }
    }

    @Composable
    fun GroupScreen() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fragmentGroups!!.Component()
            }
        }
    }

    @Composable
    fun ProfileScreen() {
        IntervisionBaseTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fragmentProfile!!.Component()
            }
        }
    }

    companion object {
        private const val TAG = "CreditsActivity"
    }
}





