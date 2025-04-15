package com.bluecode.ct037.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentInitialLoadingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitialLoadingFragment : Fragment() {
    private var _binding: FragmentInitialLoadingBinding? = null
    private val binding get() = _binding!!
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitialLoadingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            lifecycleScope.launch {
                delay(2_500)
                navController.navigate(R.id.action_initialLoadingFragment_to_homeFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}