package ru.ama.diary.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.ama.diary.domain.entity.CalendarDomModel

object CalendarDiffCallback : DiffUtil.ItemCallback<CalendarDomModel>() {

    override fun areItemsTheSame(oldItem: CalendarDomModel, newItem: CalendarDomModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CalendarDomModel, newItem: CalendarDomModel): Boolean {
        return oldItem == newItem
    }
}
