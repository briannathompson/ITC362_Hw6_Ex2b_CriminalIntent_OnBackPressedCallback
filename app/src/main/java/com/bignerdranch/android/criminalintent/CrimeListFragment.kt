package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch


private const val TAG = "CrimeListFragment"


class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                crimeListViewModel.crimes.collect { crimes ->
                    binding.crimeRecyclerView.adapter =
                        CrimeListAdapter(crimes) { crimeId ->       // 13.13 Add crimeId so we can perform the navigation
                            // 13.8 Use the findNavController function and call the navigate() function on it
                            findNavController().navigate(
                                // 13.11 Add the Safe Args Direction class function for this class to use our action
                                /*  Safe Args plugin generates Direction classes, which are the
                                    fragment's name + "Directions"
                                *   The Direction function name is based on the action's resource ID,
                                    there will be one generated function per action within the destination
                                * In this project:
                                    Direction Class: CrimeListFragmentDirections    from Class: CrimeListFragment
                                    Direction Function: showCrimeDetail             from Resource ID: R.id.show_crime_detail        */
                                CrimeListFragmentDirections.showCrimeDetail(crimeId) // 13.13 Add crimeId so we can perform the navigation
                                //R.id.show_crime_detail
                            )
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}