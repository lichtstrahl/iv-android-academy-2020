package root.iv.ivandroidacademy.app

import android.app.Application
import root.iv.ivandroidacademy.data.repository.DataRepository

class App: Application() {

    companion object {
        lateinit var dataRepository: DataRepository
    }

    override fun onCreate() {
        super.onCreate()
        dataRepository = DataRepository(this)
    }
}