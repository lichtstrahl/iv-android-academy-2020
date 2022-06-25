package root.iv.ivandroidacademy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import root.iv.ivandroidacademy.app.App

object ViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MovieDetailsViewModel::class.java -> MovieDetailsViewModel(App.dataRepository)
        else -> throw IllegalStateException("Not support ViewModel for $modelClass")
    } as T
}