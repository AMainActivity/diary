package ru.ama.diary.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import ru.ama.diary.R
import ru.ama.diary.databinding.FragmentMainBinding
import ru.ama.diary.domain.entity.CalendarDomModel
import ru.ama.diary.domain.entity.DiaryDomModelWithHour
import ru.ama.diary.presentation.adapters.CalendarAdapter
import ru.ama.diary.presentation.adapters.JobAdapter
import javax.inject.Inject


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding == null")
    private lateinit var viewModel: MainFragmentViewModel
    //private val calendar = Calendar.getInstance()
    // private var currentMonth = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as MyApplication).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main_fragment, menu)
    }

    private fun setSubTitleText(txt: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = txt
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_right -> {
                viewModel.getDatesOfNextMonth()
                true
            }

            R.id.menu_left -> {
                viewModel.getDatesOfPreviousMonth()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = null


        viewModel = ViewModelProvider(this, viewModelFactory)[MainFragmentViewModel::class.java]

        val adapter = CalendarAdapter()
        val jobAdapter = JobAdapter()
        adapter.onCalendarClickListener = object : CalendarAdapter.OnCalendarClickListener {
            override fun onCalendarClick(calendarModel: CalendarDomModel) {
                binding.tvDate.text = "дела на ${viewModel.getDayNumber(calendarModel.mDate)} " +
                        "${viewModel.getMonthName(calendarModel.mDate)} " +
                        "${viewModel.getYear(calendarModel.mDate)}"
                viewModel.getJobList(calendarModel.mDate)//if != -1
            }
        }
        jobAdapter.onJobClickListener = object : JobAdapter.OnJobClickListener {
            override fun onJobClick(jobModel: DiaryDomModelWithHour) {
                Log.e("jobModel", jobModel.toString())
                if (jobModel.name.isNotEmpty())
                    DetailJobFragment.newInstance(jobModel).show(
                        childFragmentManager, DetailJobFragment.NAME
                    )
                else
                    Toast.makeText(requireContext(), "нет записи", Toast.LENGTH_SHORT).show()
            }

        }
        binding.rvCalendar.adapter = adapter
        binding.rvCalendar.itemAnimator = null
        binding.rvJobList.adapter = jobAdapter
        binding.rvJobList.itemAnimator = null


        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        binding.rvJobList.addItemDecoration(dividerItemDecoration)
        viewModel.calendarDomModelLV.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            setSubTitleText("${viewModel.getMonthFullName(it[0].mDate)} ${viewModel.getYear(it[0].mDate)} ")

            Log.e("calendarDomModel", it.toString())
            Log.e("calendar", viewModel.calendar.time.toString())
        }
        viewModel.jobsListLD?.observe(viewLifecycleOwner) {
            viewModel.getDates()
            viewModel.getJobList(viewModel.mDateL)
        }
        viewModel.jobListDomModelLV.observe(viewLifecycleOwner) {
            jobAdapter.submitList(it)

            Log.e("jobListDomModel", it.toString())
        }

        viewModel.getJobList(System.currentTimeMillis())
        binding.tvDate.text = "дела на ${viewModel.getDayNumber(System.currentTimeMillis())} " +
                "${viewModel.getMonthName(System.currentTimeMillis())} " +
                "${viewModel.getYear(System.currentTimeMillis())}"
        viewModel.scrollPosition.observe(viewLifecycleOwner) {
            binding.rvCalendar.smoothScrollToPosition(it)

            Log.e("pos", it.toString())
        }


    }


    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}