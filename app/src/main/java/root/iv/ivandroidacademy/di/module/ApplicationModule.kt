package root.iv.ivandroidacademy.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(
    private val context: Context
) {

    @Provides
    fun context(): Context = context.applicationContext
}