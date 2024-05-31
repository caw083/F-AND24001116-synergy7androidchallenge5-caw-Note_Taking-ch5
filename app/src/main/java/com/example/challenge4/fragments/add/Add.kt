package com.example.challenge4.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.challenge4.R
import com.google.android.material.button.MaterialButton
import androidx.fragment.app.viewModels
import com.example.challenge4.databinding.FragmentAddBinding


class Add : Fragment() {
    private lateinit var TitleNotesEditText: EditText
    private lateinit var descriptionNotesEditText: EditText
    private lateinit var priorityNotesEditText:  EditText

    private lateinit var binding: FragmentAddBinding
    private val viewModel: FormViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        val addBinding = FormDataBinding().apply {
            TitleText = "Insert Data"
            ButtonText ="Add Data"
        }

        binding.formDataBinding = addBinding

        val view = binding.root
        val addNotesBtn: MaterialButton = view.findViewById(R.id.cirAddDataButton)

        // Initialize the class-level EditText variables
        TitleNotesEditText = view.findViewById(R.id.TitleInput)
        descriptionNotesEditText = view.findViewById(R.id.descriptionInput)
        priorityNotesEditText = view.findViewById(R.id.priorityInput)

        var countNotes = viewModel.getCountNotes().observe(viewLifecycleOwner) { count ->
            showToast("Count of notes: $count")
        }

        addNotesBtn.setOnClickListener {
            showAlertRequest()
        }

        return view
    }

    private fun showAlertRequest(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Insert Notes")
        alertDialogBuilder.setMessage("Are you sure you want to insert notes?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            // Call a function to handle inserting notes to the database
            insertToDatabase()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // Do nothing or handle negative response if needed
        }
        alertDialogBuilder.show()
    }

    private fun insertToDatabase(){
        // Access the class-level EditText variables
        val titleNotesText = TitleNotesEditText.text.toString()
        val descriptionNotesText =  descriptionNotesEditText.text.toString()
        val priorityNotesText = priorityNotesEditText.text.toString()

        // Alert the text
        val alertMessage = "Title: $titleNotesText\nDescription: $descriptionNotesText\nPriority: $priorityNotesText"
        AlertDialog.Builder(requireContext())
            .setTitle("Inserted Notes")
            .setMessage(alertMessage)
            .setPositiveButton("OK") { dialog, which ->
                if (viewModel.insertDataToDatabase(titleNotesText, descriptionNotesText, priorityNotesText)){
                    findNavController().navigate(R.id.action_add3_to_list2)
                }
            }.show()

    }

    fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast.show()
    }
}
