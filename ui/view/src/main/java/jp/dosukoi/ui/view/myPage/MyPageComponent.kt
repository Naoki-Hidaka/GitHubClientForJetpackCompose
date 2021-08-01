package jp.dosukoi.ui.view.myPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.ui.view.common.gray
import jp.dosukoi.ui.view.common.whiteGray
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun MyPageComponent(
    item: MyPageViewModel.RenderItem,
    onCardClick: (String) -> Unit,
    onRepositoryItemClick: (String) -> Unit,
    isRefreshing: Boolean?,
    onRefresh: () -> Unit
) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing ?: false), onRefresh = onRefresh) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            item { UserInfoCard(item.user, onCardClick) }
            itemsIndexed(item.repositoryList) { index, repository ->
                RepositoryItem(
                    repository = repository,
                    isLastItem = index == item.repositoryList.size,
                    onRepositoryItemClick = onRepositoryItemClick
                )
            }
        }
    }
}

@Composable
fun UserInfoCard(user: User, onCardClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { onCardClick(user.htmlUrl) },
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserIcon(user.avatarUrl)
            UserInfo(user)
        }
    }
}

@Composable
fun UserIcon(imageUrl: String) {
    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                transformations(CircleCropTransformation())
            }
        ),
        contentDescription = null,
        modifier = Modifier
            .size(125.dp)
            .padding(top = 16.dp, start = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun UserInfo(user: User) {
    Column(
        modifier = Modifier
            .padding(start = 24.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        Text(user.login, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
        Text(
            user.company ?: "",
            modifier = Modifier.padding(top = 18.dp),
            style = TextStyle(fontSize = 14.sp)
        )
        Text(user.bio ?: "", Modifier.padding(top = 6.dp), style = TextStyle(fontSize = 14.sp))
    }
}

@Composable
fun RepositoryItem(
    repository: Repository,
    isLastItem: Boolean,
    onRepositoryItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRepositoryItemClick(repository.htmlUrl)
            }
    ) {
        Text(
            repository.fullName,
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp)
        )
        Text(
            repository.description ?: "",
            style = TextStyle(fontSize = 14.sp, color = gray),
            modifier = Modifier.padding(top = 6.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
        )
        if (!isLastItem) Divider(color = whiteGray)
    }
}