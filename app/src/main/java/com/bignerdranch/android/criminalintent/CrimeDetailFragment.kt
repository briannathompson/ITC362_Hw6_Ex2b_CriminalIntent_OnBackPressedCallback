package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeDetailBinding
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
            }

            crimeDate.apply {
                // 13.15 Delete references to old crime text = crime.date.toString()
                isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                // 13.15 Delete references to old crime crime = crime.copy(isSolved = isChecked)
            }
        }
    }

    // Null out references to the view
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}