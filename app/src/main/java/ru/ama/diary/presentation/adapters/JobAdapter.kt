package ru.ama.diary.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        /* val binding = ItemJobInfoWithDataBinding.inflate(
             LayoutInflater.from(parent.context),
             parent,
             false
         )*/
        /*val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )*/
        //return JobViewHolder(binding)
    }

    /* override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         TODO("Not yet implemented")
     }*/


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mJob = getItem(position)
        val itemtype = holder.itemViewType
        /*val binding =*/ when (itemtype) {
            VIEW_TYPE_NO_DATA -> {
                val binding = (holder as JobViewHolderNoData).binding
                with(holder.binding) {
                    with(mJob) {
                        tvTimePeriod.text = "$timeStart-$timeEnd"
                        tvTitle.text = "$name"

                        root.setOnClickListener {
                            onJobClickListener?.onJobClick(this)
                        }
                    }
                }
            }

            VIEW_TYPE_WITH_DATA -> {
                val binding = (holder as JobViewHolderWithData).binding
                with(holder.binding) {
                    with(mJob) {
                        tvTimePeriod.text = "$timeStart-$timeEnd"
                        tvTitle.text = "$name"
                        /*tvInfo.text = description + " $dateStart - $dateFinish "
                        if(name.isNotEmpty())
                            frgmntJobFl.visibility= View.VISIBLE
                        else
                            frgmntJobFl.visibility= View.GONE
*/
                        root.setOnClickListener {
                            onJobClickListener?.onJobClick(this)
                        }
                    }
                }
            }

            else -> throw RuntimeException("Unknown view type: $itemtype")
        }
        /*with(holder.binding) {
            with(mJob) {
                tvTimePeriod.text = "$timeStart-$timeEnd"
                tvTitle.text = "$name"
                tvInfo.text = description + " $dateStart - $dateFinish "
                if(name.isNotEmpty())
                    frgmntJobFl.visibility= View.VISIBLE
                else
                    frgmntJobFl.visibility= View.GONE

                root.setOnClickListener {
                    onJobClickListener?.onJobClick(this)
                }
            }
        }*/
    }

    private fun getDayNumber(date: Date): String =
        SimpleDateFormat("dd", Locale.getDefault()).format(date)

    private fun getDay3LettersName(date: Date): String =
        SimpleDateFormat("EE", Locale.getDefault()).format(date)

    companion object {

        const val VIEW_TYPE_WITH_DATA = 777
        const val VIEW_TYPE_NO_DATA = 555

    }

    interface OnJobClickListener {
        fun onJobClick(jobModel: DiaryDomModelWithHour)
    }
}