package root.iv.ivandroidacademy.ui.component.diffutil

import androidx.recyclerview.widget.DiffUtil
import root.iv.ivandroidacademy.data.model.DiffUtilModel

class DiffUtilCallback(
    private val oldContent: List<DiffUtilModel>,
    private val newContent: List<DiffUtilModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldContent.size

    override fun getNewListSize(): Int = newContent.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldContent[oldItemPosition].id() == newContent[newItemPosition].id()

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldContent[oldItemPosition].same(newContent[newItemPosition])
}