package com.csu_itc303_team1.adhdtaskmanager.ui.todo_detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoDetailViewModel @Inject constructor(

): ViewModel() {

    var todoId: Long = 0L
}
