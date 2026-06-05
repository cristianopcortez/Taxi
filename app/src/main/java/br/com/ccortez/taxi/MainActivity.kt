package br.com.ccortez.taxi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.ccortez.core.common.utils.ColorBackground
import br.com.ccortez.core.datastore.ThemeMode
import br.com.ccortez.taxi.navigation.AppNavGraph
import br.com.ccortez.taxi.navigation.NavigationProvider
import br.com.ccortez.taxi.ui.theme.TaxiTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationProvider: NavigationProvider

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()
            val navController = rememberNavController()
            App(navHostController = navController, navigationProvider, themeMode)
        }
    }
}

@Composable
fun App(
    navHostController: NavHostController,
    navigationProvider: NavigationProvider,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
) {
    val isDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = ColorBackground)

    TaxiTheme(darkTheme = isDark) {
        Surface(
            modifier = Modifier.background(ColorBackground).fillMaxSize(),
            color = ColorBackground
        ) {
            AppNavGraph(navController = navHostController, navigationProvider = navigationProvider)
        }
    }
}
