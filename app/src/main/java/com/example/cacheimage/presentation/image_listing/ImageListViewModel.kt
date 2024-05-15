package com.example.cacheimage.presentation.image_listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {


    private var _state = MutableStateFlow(State())
    val state = _state.asStateFlow()


    init {
       getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            repository.getList(Dispatchers.IO).collect {
                it.onSuccess {
                    _state.value = state.value.copy(
                        images = it,
                        error = null
                    )
                }

                it.onFailure {
                    _state.value = state.value.copy(
                        error = it.message
                    )
                }

            }
        }
    }


    data class State(
        val images : List<Image> = arrayListOf(),
        val error: String? = null
    )

    fun clearError(){
        _state.value = _state.value.copy(
            error = null
        )
    }

}