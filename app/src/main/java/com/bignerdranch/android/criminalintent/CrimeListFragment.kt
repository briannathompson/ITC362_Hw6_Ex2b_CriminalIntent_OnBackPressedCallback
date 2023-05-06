package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


private const val TAG = "CrimeListFragment"

// Make CrimeListFragment a subclass of Fragment (androidx.fragment.app.Fragment)
class CrimeListFragment : Fragment() {


    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    // 12.5 Create a variable for an instance of the Job class
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}") // Logs the number of crimes found in CrimeListViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        // Set up the LayoutManager
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        // Instantiate an instance of CrimeListAdapter with our crime data AND connect it to the RecyclerView
        val crimes = crimeListViewModel.crimes
        val adapter = CrimeListAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter

        return binding.root
    }

    // 12.5 call the coroutine by using job
    override fun onStart() {
        super.onStart()
        job = viewLifecycleOwner.lifecycleScope.launch {
            val crimes = crimeListViewModel.loadCrimes()
            binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes)
        }
    }
    // 12.5 End the coroutine
    override fun onStop() {
        super.onStop()
        job?.cancel()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}