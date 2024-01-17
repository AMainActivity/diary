package ru.ama.diary.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import ru.ama.diary.R
import ru.ama.diary.databinding.CalendarItemBinding
import ru.ama.diary.domain.entity.CalendarDomModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarAdapter(
) : ListAdapter<CalendarDomModel, CalendarViewHolder>(CalendarDiffCallback) {

    var onCalendarClickListener: OnCalendarClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarViewHolder(binding)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<CalendarDomModel>,
        currentList: MutableList<CalendarDomModel>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    fun getItems(position: Int): CalendarDomModel {
        return getItem(position)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val mJob = getItem(position)
        with(holder.binding) {
            with(mJob) {
                tvDateCalendarItem.text = getDayNumber(mDate)
                tvDayCalendarItem.text = getDay3LettersName(mDate)
                if (mCount == INT_ZERO) {
                    lvIsJobExist.visibility = View.INVISIBLE
                    tvCount.text = EMPTY_STRING
                } else {
                    lvIsJobExist.visibility = View.VISIBLE
                    tvCount.text = mCount.toString()
                }

                val formatter = SimpleDateFormat("dd.MM.yyyy")
                if (formatter.format(mDate)
                        .compareTo(formatter.format(Calendar.getInstance().time)) == INT_ZERO
                ) {
                    holder.binding.clCalendarItem.strokeColor =
                        ContextCompat.getColor(
                            tvDateCalendarItem.context,
                            android.R.color.holo_red_light
                        );
                } else {
                    holder.binding.clCalendarItem.strokeColor =
                        ContextCompat.getColor(
                            tvDateCalendarItem.context,
                            android.R.color.darker_gray
                        );
                }
                root.setOnClickListener {
                    onCalendarClickListener?.onCalendarClick(this)
                }
            }
        }
    }

    private fun getDayNumber(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
    }

    private fun getDay3LettersName(date: Long): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return SimpleDateFormat("EE", Locale.getDefault()).format(calendar.time)
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val INT_ZERO = 0
    }

    interface OnCalendarClickListener {
        fun onCalendarClick(calendarModel: CalendarDomModel)
    }
}
