package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.launch

class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    /* Challenge Code*/
    /* Where to put OnBackPressedCallback: https://medium.com/@valentinerutto/handling-onbackpressed-in-fragment-3913046c377e */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create the OnBackPressedCallback
        /* https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher */
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // If the crime_title EditText is empty, then show a toast that asks the user to describe the crime
                if (binding.crimeTitle.text.toString() == "") {
                    /* How to display toast widget in a fragment:
                        https://stackoverflow.com/questions/63465960/how-to-display-a-toast-widget-in-a-fragment
                        https://alvinalexander.com/source-code/android/how-show-android-toast-message-fragment/ */
                    Toast.makeText(this@CrimeDetailFragment.requireActivity(), R.string.empty_crime_title_response, Toast.LENGTH_SHORT).show()
                }
                // Else, if there is text in the crimeTitle EditText, pop CrimeDetailFragment off the back stack with NavController
                else {
                    // Use the NavController to pop off the CrimeDetailFragment
                    /* https://developer.android.com/reference/androidx/navigation/NavController#popBackStack() */
                    findNavController().popBackStack()
                }
            }
        }
        // Add the OnBackPressedCallback object ("callback") to the back stack manager
        /* https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher#addCallback(androidx.activity.OnBackPressedCallback) */
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


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