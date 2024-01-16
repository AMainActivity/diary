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
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.ama.diary.R
import ru.ama.diary.databinding.DatePickerDaysBinding
import ru.ama.diary.databinding.FragmentAddJobBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject


class AddJobFragment : DialogFragment() {

    private var _binding: FragmentAddJobBinding? = null
    private val binding: FragmentAddJobBinding
        get() = _binding ?: throw RuntimeException("FragmentAddJobBinding == null")

    private lateinit var viewModel: AddJobViewModel
    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun showPopupDatePicker(anchor: View) {
        Toast.makeText(requireContext(), "showPopupDatePicker", Toast.LENGTH_SHORT).show()
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
        val binding2 = DatePickerDaysBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding2.frgmntAddJobDp.setOnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth ->
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val s = formatter.format(calendar.time)
                binding.frgmntAddJobEtDate.setText(s)
                popupWindow.dismiss()

            }
        } else {
            val cal = Calendar.getInstance()
            cal.timeInMillis = System.currentTimeMillis()
            binding2.frgmntAddJobDp.init(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ) { datePicker, year, monthOfYear, dayOfMonth ->
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val s = formatter.format(calendar.time)
                binding.frgmntAddJobEtDate.setText(s)
                popupWindow.dismiss()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.windowLayoutType =
                WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        }

        popupWindow.contentView = binding2.root
        popupWindow.showAsDropDown(anchor)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddJobBinding.inflate(inflater, container, false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.dialog_animation_pd
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        viewModel = ViewModelProvider(this, viewModelFactory)[AddJobViewModel::class.java]
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hours_array, R.layout.spinner_item
        )

        binding.frgmntAddJobCalendar.setOnClickListener {
            showPopupDatePicker(it)
        }
        binding.frgmntAddJobTimeSpinner.adapter = adapter
        observeViewModel()
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
        binding.frgmntAddJobButAdd.setOnClickListener {
            viewModel.tryJobSave(
                binding.frgmntAddJobEtDate.text.toString(),
                binding.frgmntAddJobTimeSpinner.selectedItem.toString(),
                binding.frgmntAddJobEtName.text.toString(),
                binding.frgmntAddJobEtDescription.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.isSuccessSave.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }
        viewModel.errorName.observe(viewLifecycleOwner) {
            val message = if (it) {
                "введите"
            } else {
                null
            }
            binding.frgmntAddJobEtName.error = message
        }
        viewModel.errorDate.observe(viewLifecycleOwner) {
            val message = if (it) {
                "введите"
            } else {
                null
            }
            binding.frgmntAddJobEtDate.error = message
        }
        viewModel.errorDescription.observe(viewLifecycleOwner) {
            val message = if (it) {
                "введите"// String.format(getString(R.string.set_format), TIME_PERIODIC_LENGTH)
            } else {
                null
            }
            binding.frgmntAddJobEtDescription.error = message
        }
    }

    companion object {
        private const val ARG_ADD_JOB = "addJob"
        const val NAME = "AddJobFragment"
        fun newInstance(): AddJobFragment {
            return AddJobFragment()
        }
    }
}
