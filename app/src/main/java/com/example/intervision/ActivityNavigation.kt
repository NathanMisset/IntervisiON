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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


@Suppress("DEPRECATION")
class ActivityNavigation : ComponentActivity() {
    private var fragmentHome: FragmentHome? = null
    private var fragmentGroups: FragmentGroups? = null
    private var fragmentProfile: FragmentProfile? = null
    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private var storage: FirebaseStorage? = null
    private var auth: FirebaseAuth? = null

    sealed class Screen(val route: String) {
        data object Home : Screen("home")
        data object Group : Screen("group")
        data object Profile : Screen("profile")
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
                    ) //content:
                    { paddingValues ->
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        fragmentHome = FragmentHome()
        fragmentHome!!.init(user!!, db!!, this)
        fragmentGroups = FragmentGroups()
        fragmentGroups!!.init(auth!!,db!!,storage!!,this)
        fragmentProfile = FragmentProfile()
        fragmentProfile!!.init(user!!,storage!!,db!!,this)
        Log.d("navigation", "OnCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("navigation", "OnStart")
        Handler().postDelayed({
            setContent {
                MyApplicationTheme {
                    Log.d("navigation", "setContent")
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { BottomAppBarWithNavigation(navController = navController) },
                    ) //content:
                    { paddingValues ->
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
                // Create your Profile screen UI here
                fragmentProfile!!.Component()
            }
        }
    }

}

//modifier = Modifier.height(10.dp),
@Composable
fun BottomAppBarWithNavigation(navController: NavHostController) {
    BottomAppBar {
        Row (horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
        ){
            NavigationItem(
                navController,
                ActivityNavigation.Screen.Home,
                Icons.Default.Home,
                "Home"
            )
            NavigationItem(
                navController,
                ActivityNavigation.Screen.Group,
                Icons.Default.Group,
                "Groep"
            )
            NavigationItem(
                navController,
                ActivityNavigation.Screen.Profile,
                Icons.Default.AccountCircle,
                "Profile"
            )
        }
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: ActivityNavigation.Screen,
    icon: ImageVector,
    label: String
) {
    IconButton(
        onClick = { navController.navigate(screen.route) }
    ) {
        Icon(imageVector = icon, contentDescription = label)
    }
}


