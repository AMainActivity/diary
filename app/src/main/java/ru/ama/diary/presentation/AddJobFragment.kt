package ru.ama.diary.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.diary.R
import ru.ama.diary.databinding.DatePickerDaysBinding
import ru.ama.diary.databinding.FragmentAddJobBinding
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject


class AddJobFragment : DialogFragment() {

    private var _binding: FragmentAddJobBinding? = null
    private val binding: FragmentAddJobBinding
        get() = _binding ?: throw RuntimeException("FragmentAddJobBinding == null")

    private lateinit var viewModel: AddJobViewModel
    private var jobModel: DiaryDomModelWithHour? = null
    private var mDate = 0L
    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }
    private val hoursArray by lazy {
        resources.getStringArray(
            R.array.hours_array
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onStart() {
        super.onStart()
        setDialogLayout()
    }

    private fun setDialogLayout() {
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setDatePickerChanged(popupWindow: PopupWindow) =
        DatePicker.OnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth ->
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            val s = formatter.format(calendar.time)
            binding.frgmntAddJobEtDate.setText(s)
            popupWindow.dismiss()
        }

    private fun showPopupDatePicker(anchor: View) {
        val popupWindow = PopupWindow(requireContext())
        popupWindow.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.nulldr,
                null
            )
        )
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        val bindingDatePicker = DatePickerDaysBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bindingDatePicker.frgmntAddJobDp.setOnDateChangedListener(
                setDatePickerChanged(
                    popupWindow
                )
            )
        } else {
            val cal = Calendar.getInstance()
            cal.timeInMillis = System.currentTimeMillis()
            bindingDatePicker.frgmntAddJobDp.init(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                setDatePickerChanged(
                    popupWindow
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.windowLayoutType =
                WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }

        popupWindow.contentView = bindingDatePicker.root
        popupWindow.showAsDropDown(anchor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddJobBinding.inflate(inflater, container, false)
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

    private fun setChangeListeners() {
        binding.frgmntAddJobEtDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.resetError(JobAttributeNames.JOB_DATE)
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.validateInputData(s.toString(), JobAttributeNames.JOB_DATE)
            }
        })
        binding.frgmntAddJobEtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.resetError(JobAttributeNames.JOB_NAME)
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.validateInputData(s.toString(), JobAttributeNames.JOB_NAME)
            }
        })
        binding.frgmntAddJobEtDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.resetError(JobAttributeNames.JOB_DESCRIPTION)
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.validateInputData(s.toString(), JobAttributeNames.JOB_DESCRIPTION)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[AddJobViewModel::class.java]

        setSpinnerAdapter()
        binding.frgmntAddJobCalendar.setOnClickListener {
            showPopupDatePicker(it)
        }
        observeViewModel()
        setChangeListeners()
        setButtonAddClick()
        setTextForViews()
    }

    private fun setSpinnerAdapter() {
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item, hoursArray
        )
        binding.frgmntAddJobTimeSpinner.adapter = adapter
    }

    private fun setButtonAddClick() {
        binding.frgmntAddJobButAdd.setOnClickListener {
            viewModel.tryJobSave(
                binding.frgmntAddJobEtDate.text.toString(),
                binding.frgmntAddJobTimeSpinner.selectedItem.toString(),
                binding.frgmntAddJobEtName.text.toString(),
                binding.frgmntAddJobEtDescription.text.toString()
            )
        }
    }

    private fun setTextForViews() {
        jobModel?.let {
            binding.frgmntAddJobEtDescription.setText(it.description)
            binding.frgmntAddJobEtDate.setText(viewModel.getDate(mDate))
            binding.frgmntAddJobTimeSpinner.setSelection(hoursArray.indexOf(viewModel.parseTime(it.timeStart)))
            Log.e("indexOf", (hoursArray.indexOf(it.timeStart)).toString() + " " + it.timeStart)
        }
    }

    private fun observeViewModel() {
        viewModel.isSuccessSave.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }
        viewModel.errorName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.frgmnt_addjob_empty)
            } else {
                null
            }
            binding.frgmntAddJobEtName.error = message
        }
        viewModel.errorDate.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.frgmnt_addjob_empty)
            } else {
                null
            }
            binding.frgmntAddJobEtDate.error = message
        }
        viewModel.errorDescription.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.frgmnt_addjob_empty)
            } else {
                null
            }
            binding.frgmntAddJobEtDescription.error = message
        }
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_ADD_JOB) && !args.containsKey(ARG_ADD_JOB_DATE)) {
            throw RuntimeException(PARSE_ERROR)
        }
        args.getParcelable<DiaryDomModelWithHour>(ARG_ADD_JOB)?.let {
            jobModel = it
        }
        mDate = args.getLong(ARG_ADD_JOB_DATE)
    }

    companion object {
        private const val ARG_ADD_JOB = "addJob"
        private const val ARG_ADD_JOB_DATE = "addJobDate"
        const val PARSE_ERROR = "Required param jobInfo is absent"
        const val NAME = "AddJobFragment"
        fun newInstance(jobModel: DiaryDomModelWithHour?, mDate: Long): AddJobFragment {
            return AddJobFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ADD_JOB, jobModel)
                    putLong(ARG_ADD_JOB_DATE, mDate)
                }
            }
        }
    }
}
