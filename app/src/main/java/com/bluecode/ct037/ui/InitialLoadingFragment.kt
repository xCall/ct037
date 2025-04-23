package com.bluecode.ct037.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentInitialLoadingBinding

class InitialLoadingFragment : Fragment() {

    private var _binding: FragmentInitialLoadingBinding? = null
    private val binding get() = _binding!!

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

            Handler(Looper.getMainLooper()).postDelayed({
                parentFragmentManager.beginTransaction().replace(R.id.mainInitialLoading,HomeFragment()).commit()
            },3000)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}