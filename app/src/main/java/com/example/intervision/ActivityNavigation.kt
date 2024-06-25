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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intervision.ui.MyApplicationTheme
import com.example.intervision.ui.UiString
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
        Log.d("navigation", "OnStart")
        Handler().postDelayed({
            setContent {
                MyApplicationTheme {
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
                MyApplicationTheme {
                    Log.d(TAG, "setContent")
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
        BottomAppBar {
            Row (horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationItem(
                    navController,
                    Screen.Home,
                    Icons.Default.Home,
                    UiString.homeLabelNavigation
                )
                NavigationItem(
                    navController,
                    Screen.Group,
                    Icons.Default.Group,
                    UiString.groupLabelNavigation
                )
                NavigationItem(
                    navController,
                    Screen.Profile,
                    Icons.Default.AccountCircle,
                    UiString.profileLabelNavigation
                )
            }
        }
    }

    @Composable
    fun NavigationItem(
        navController: NavHostController,
        screen: Screen,
        icon: ImageVector,
        label: String
    ) {
        IconButton(
            onClick = { navController.navigate(screen.route) }
        ) {
            Icon(imageVector = icon, contentDescription = label)
        }
    }

    @Composable
    fun HomeScreen() {
            MyApplicationTheme {
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
        MyApplicationTheme {
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
        MyApplicationTheme {
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





