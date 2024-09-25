package com.example.map.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.map.domain.Comment
import com.example.map.domain.DeleteCommentsFromDb
import com.example.map.domain.GetCommentsListUseCase
import com.example.map.domain.GetOnePhotoInfoUseCase
import com.example.map.domain.PostCommentUseCase
import com.example.map.presentation.DetailPhotoInfoScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailPhotoInfoViewModel @Inject constructor(
    private val getOnePhotoInfoUseCase: GetOnePhotoInfoUseCase,
    private val posCommentUseCase: PostCommentUseCase,
    private val getCommentsUseCase: GetCommentsListUseCase,
    private val deleteCommentsFromDbUseCase: DeleteCommentsFromDb

) : ViewModel() {


    private val _detailPhotosScreenState = MutableLiveData<DetailPhotoInfoScreenState>()

    val detailPhotosScreenState: MutableLiveData<DetailPhotoInfoScreenState>
        get() = _detailPhotosScreenState


    private val coroutineExceptionHandlerForLoadingOnePhoto =
        CoroutineExceptionHandler { _, throwable ->
            Log.d("DetailPhotoInfoViewModel", throwable.message.toString())
        }
    private val coroutineExceptionHandlerForLoadingComments =
        CoroutineExceptionHandler { _, throwable ->
            Log.d("DetailPhotoInfoViewModel", throwable.message.toString())
        }

    private val coroutineExceptionHandlerForPostingComment =
        CoroutineExceptionHandler { _, throwable ->
            Log.d("DetailPhotoInfoViewModel", throwable.message.toString())
        }

    fun loadOnePhoto(id: Int) {
        viewModelScope.launch(coroutineExceptionHandlerForLoadingOnePhoto) {
            val photo = getOnePhotoInfoUseCase(id)
            _detailPhotosScreenState.value = DetailPhotoInfoScreenState.LoadedPhotos(photo)
        }
    }

    fun postComment(accessToken: String, text: String, imageId: Int, page: Int) {
        viewModelScope.launch(coroutineExceptionHandlerForPostingComment) {
            val comment = Comment(text = text)
            posCommentUseCase(accessToken, comment, imageId)
            getCommentsList(accessToken, imageId, page)
        }
    }

    fun getCommentsList(
        accessToken: String,
        imageId: Int,
        page: Int
    ) {
        viewModelScope.launch(coroutineExceptionHandlerForLoadingComments) {
            val commentsList = getCommentsUseCase(accessToken, imageId, page)
            _detailPhotosScreenState.value = DetailPhotoInfoScreenState.LoadedComments(commentsList)
        }
    }

    fun deleteCommentsFromDb() {
        viewModelScope.launch {
            deleteCommentsFromDbUseCase()
        }
    }
}