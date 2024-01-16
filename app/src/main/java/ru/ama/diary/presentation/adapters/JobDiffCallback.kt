package ru.ama.diary.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.diary.domain.entity.DiaryDomModelWithHour

object JobDiffCallback : DiffUtil.ItemCallback<DiaryDomModelWithHour>() {

    override fun areItemsTheSame(
        oldItem: DiaryDomModelWithHour,
        newItem: DiaryDomModelWithHour
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DiaryDomModelWithHour,
        newItem: DiaryDomModelWithHour
    ): Boolean {
        return oldItem == newItem
    }
}
