package jp.dosukoi.ui.view.top

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jp.dosukoi.ui.view.common.AppBarScaffold
import jp.dosukoi.ui.view.common.black
import jp.dosukoi.ui.view.common.gray
import jp.dosukoi.ui.view.common.white
import jp.dosukoi.ui.view.list.TopPageScreen
import jp.dosukoi.ui.view.myPage.MyPageScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun TopScreen(
    myPageViewModel: MyPageViewModel,
    onLoginButtonClick: () -> Unit,
    onCardClick: (String) -> Unit,
    onRepositoryItemClick: (String) -> Unit
) {
    val bottomNavigationItemList = listOf(
        TopScreens.Search,
        TopScreens.MyPage
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    AppBarScaffold(title = "GitHubClient", bottomNavigation = {
        BottomNavigation(
            backgroundColor = white
        ) {
            bottomNavigationItemList.forEach { item ->
                BottomNavigationItem(
                    selected = item.route == currentRoute,
                    onClick = { navController.navigate(item.route) },
                    icon = { Icon(imageVector = item.icon, contentDescription = null) },
                    label = { Text(text = item.title) },
                    selectedContentColor = black,
                    unselectedContentColor = gray.copy(alpha = 0.4f)
                )
            }
        }
    }, content = {
        NavHost(navController = navController, startDestination = TopScreens.MyPage.route) {
            composable(TopScreens.Search.route) {
                TopPageScreen()
            }
            composable(TopScreens.MyPage.route) {
                MyPageScreen(
                    myPageViewModel,
                    onLoginButtonClick,
                    onCardClick,
                    onRepositoryItemClick
                )
            }
        }
    })
}

sealed class TopScreens(val route: String, val title: String, val icon: ImageVector) {
    object Search : TopScreens("search", "Search", Icons.Filled.Search)
    object MyPage : TopScreens("myPage", "MyPage", Icons.Filled.Person)
}