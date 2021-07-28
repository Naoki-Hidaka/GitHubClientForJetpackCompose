package jp.dosukoi.ui.viewmodel.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopScreenViewModel @Inject constructor() : ViewModel() {

    val bottomNavigationItemList = listOf(
        "Search",
        "MyPage"
    )

    val selectedItem = MutableLiveData<Int>()

    fun onItemClick(index: Int) {
        selectedItem.value = index
    }
}