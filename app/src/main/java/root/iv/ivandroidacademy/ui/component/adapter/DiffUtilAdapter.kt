package root.iv.ivandroidacademy.ui.component.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.model.DiffUtilModel
import root.iv.ivandroidacademy.ui.component.diffutil.DiffUtilCallback

abstract class DiffUtilAdapter<T: DiffUtilModel, VH: RecyclerView.ViewHolder>(
    protected var content: List<T>,
    protected val listener: (T) -> Unit
): RecyclerView.Adapter<VH>() {

    fun resetData(content: List<T>) {
        val callback = DiffUtilCallback(this@DiffUtilAdapter.content, content)
        this@DiffUtilAdapter.content = content

        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(this@DiffUtilAdapter)
    }
}