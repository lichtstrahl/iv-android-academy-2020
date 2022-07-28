package root.iv.ivandroidacademy.di.module

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import dagger.Module
import dagger.Provides
import root.iv.ivandroidacademy.ui.notify.NotifyPublisher

@ExperimentalPagingApi
@Module(includes = [ApplicationModule::class])
class NotifyModule {

    @Provides
    fun notifyPublisher(ctx: Context) = NotifyPublisher(ctx)
}