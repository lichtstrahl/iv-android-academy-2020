package root.iv.ivandroidacademy.work

import androidx.work.Constraints
import androidx.work.NetworkType

object WorkConstraints {

    val updateCacheConstraint = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
//        .setRequiresCharging(true)
        .build()
}