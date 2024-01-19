package ru.ama.diary.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.diary.R
import ru.ama.diary.databinding.FragmentDetailJobBinding
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import javax.inject.Inject


class DetailJobFragment : DialogFragment() {

    private var _binding: FragmentDetailJobBinding? = null
    private val binding: FragmentDetailJobBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailJobBinding == null")

    private lateinit var viewModel: DetailJobViewModel
    private lateinit var jobModel: DiaryDomModelWithHour
    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onStart() {
        super.onStart()
        setDialogLayout()
    }

    private fun setDialogLayout() {
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailJobBinding.inflate(inflater, container, false)
        setDialogAttributes()
        return binding.root
    }

    private fun setDialogAttributes() {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.dialog_animation_pd
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailJobViewModel::class.java]
        setTextInViews()
    }

    private fun setTextInViews() {
        binding.jobDetailTvName.text = jobModel.name
        binding.jobDetailTvDatetime.text =
            getString(
                R.string.frgmnt_detail_datetime,
                viewModel.getDate(jobModel.dateStart),
                jobModel.timeStart,
                jobModel.timeEnd
            )
        binding.jobDetailTvInfo.text = jobModel.description
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_JOB_INFO)) {
            throw RuntimeException(PARSE_ERROR)
        }
        args.getParcelable<DiaryDomModelWithHour>(ARG_JOB_INFO)?.let {
            jobModel = it
        }
    }


    companion object {
        private const val ARG_JOB_INFO = "jobInfo"
        const val NAME = "DetailJobFragment"
        const val PARSE_ERROR = "Required param jobInfo is absent"
        fun newInstance(jobModel: DiaryDomModelWithHour): DetailJobFragment {
            return DetailJobFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_JOB_INFO, jobModel)
                }
            }
        }
    }
}