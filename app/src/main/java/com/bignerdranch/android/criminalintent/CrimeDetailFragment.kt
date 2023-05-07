package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch
import java.util.*

// 13.15 Delete references to old crime private const val TAG = "CrimeDetailFragment" // 13.14 Add a TAG for CrimeDetailFragment

class CrimeDetailFragment : Fragment() {

    // Create a nullable backing property (called _binding) and change the property to become a computed property
    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    //  13.15 Delete references to the old crime private lateinit var crime: Crime // property for the Crime instance

    // 13.14 Add a class property called args using the navArgs property delegate
    private val args: CrimeDetailFragmentArgs by navArgs()

    // 13.18 add this class property that allows you to access the CrimeDetailViewModel
    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }


    /* 13.15 Delete references to old crime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            isSolved = false
        )
        // 13.14 Use the TAG and add a message that gives you the ID from the crime you clicked on
        Log.d(TAG, "The crime ID is: ${args.crimeId}")
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->   // lambda arguments named _ are ignored; we only care about text
                // 13.15 Delete references to old crime crime = crime.copy(title = text.toString())

                // 13.21 Hook the UI up to our updateCrime function
                // if text changes, call back to crimeDetailViewModel and run updateCrime function
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())      // changes title
                }

            }

            crimeDate.apply {
                // 13.15 Delete references to old crime text = crime.date.toString()
                isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                // 13.15 Delete references to old crime crime = crime.copy(isSolved = isChecked)

                // 13.21 Hook the UI up to our updateCrime function
                // if checkbox changes, call back to crimeDetailViewModel and run updateCrime function
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)       // changes solved status
                }
            }
        }

        // 13.19 Add a lifecycle viewer that we will use to retain data
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let { updateUi(it) }
                }
            }
        }
    }

    // Null out references to the view
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 13.19 Add a update ui function
    private fun updateUi(crime: Crime) {        // takes the whole crime object
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeSolved.isChecked = crime.isSolved
        }
    }
}