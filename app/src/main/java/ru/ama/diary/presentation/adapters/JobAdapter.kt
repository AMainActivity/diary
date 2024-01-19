package ru.ama.diary.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ama.diary.R
import ru.ama.diary.databinding.ItemJobInfoNoDataBinding
import ru.ama.diary.databinding.ItemJobInfoWithDataBinding
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class JobAdapter : ListAdapter<DiaryDomModelWithHour, RecyclerView.ViewHolder>(JobDiffCallback) {

    var onJobClickListener: OnJobClickListener? = null

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.name.isNotEmpty()) {
            VIEW_TYPE_WITH_DATA
        } else {
            VIEW_TYPE_NO_DATA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NO_DATA -> JobViewHolderNoData(
                ItemJobInfoNoDataBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_WITH_DATA -> JobViewHolderWithData(
                ItemJobInfoWithDataBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw RuntimeException("Unknown view type: $viewType")
        }

    }

    private fun setItemTextAndClick(
        mTitle: TextView,
        mTimePeriod: TextView,
        mRoot: View,
        mJob: DiaryDomModelWithHour
    ) {
        with(mJob) {
            mTimePeriod.text = mTimePeriod.context.getString(
                R.string.job_adapter_time_period,
                timeStart,
                timeEnd
            )
            mTitle.text = name
            mRoot.setOnClickListener {
                onJobClickListener?.onJobClick(mJob)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mJob = getItem(position)
        val itemType = holder.itemViewType
        when (itemType) {
            VIEW_TYPE_NO_DATA -> {
                val binding = (holder as JobViewHolderNoData).binding
                with(holder.binding) {
                    setItemTextAndClick(tvTitle, tvTimePeriod, root, mJob)
                }
            }

            VIEW_TYPE_WITH_DATA -> {
                val binding = (holder as JobViewHolderWithData).binding
                with(binding) {
                    setItemTextAndClick(tvTitle, tvTimePeriod, root, mJob)
                }
            }

            else -> throw RuntimeException("Unknown view type: $itemType")
        }

    }

    companion object {
        const val VIEW_TYPE_WITH_DATA = 777
        const val VIEW_TYPE_NO_DATA = 555
    }

    interface OnJobClickListener {
        fun onJobClick(jobModel: DiaryDomModelWithHour)
    }
}
