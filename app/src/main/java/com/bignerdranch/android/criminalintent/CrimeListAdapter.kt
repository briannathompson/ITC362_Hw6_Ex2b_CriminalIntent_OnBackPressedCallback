package com.bignerdranch.android.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import java.util.*


class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {

    /* When we click the crime, we want to grab the id of the item that we clicked.
    * Adding crimeId: UUID allows us to grab the crime when we call onCrimeClicked */
    // 13.6 pass lambda expression called onCrimeClicked into the bind function in CrimeHolder
    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit){  // 13.12 Add crimeId: crime as an argument to the onCrimeClicked parameter (passing ID back from the adapter)
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            /* 13.6 Remove toast
            Toast.makeText(
                binding.root.context,
                "${crime.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()*/
            // 13.6 Add a call to onCrimeClicked()
            onCrimeClicked(crime.id) // 13.12 Get the crime's id
        }

        // If the crime is solved, make the crimeSolved ImageView visible; else make it gone
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class CrimeListAdapter(
    private val crimes: List<Crime>,
    // 13.7 Add onCrimeClicked as a constructor parameter
    private val onCrimeClicked: (crimeId: UUID) -> Unit // 13.12 Add crimeId: crime as an argument to onCrimeClicked
) : RecyclerView.Adapter<CrimeHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]

        // 13.7 Add onCrimeClicked to the holder.bind function
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = crimes.size
}
