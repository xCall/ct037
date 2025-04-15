package com.bluecode.ct037.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentSettingsActivityDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsActivityDialogFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentSettingsActivityDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsActivityDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){}
    }

    companion object {
        const val TAG = "SettingsActivityDialogFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}