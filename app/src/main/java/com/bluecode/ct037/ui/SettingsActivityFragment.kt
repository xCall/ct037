package com.bluecode.ct037.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentSettingsActivityBinding
import android.widget.Toast


class SettingsActivityFragment : Fragment() {

    private var _binding : FragmentSettingsActivityBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsActivityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDate()

        with(binding) {
            bntBackToHome.setOnClickListener{
                parentFragmentManager.beginTransaction().replace(R.id.settingsHome, HomeFragment()).commit()
            }

            btnSaveSettings.setOnClickListener{
                saveData()
                Toast.makeText(requireContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData() {
        var prefs = requireContext().getSharedPreferences("settings_app", Context.MODE_PRIVATE)
        val uptime = binding.tietValueAverageFightTime.text.toString().toIntOrNull() ?: 0
        val restTime = binding.tietValueTimeForRest.text.toString().toIntOrNull() ?: 0

        prefs.edit().putInt("uptime", uptime).putInt("restTime", restTime).apply()
    }

    private fun loadDate() {
        try {
            val prefs = requireContext().getSharedPreferences("settings_app", Context.MODE_PRIVATE)

            val tempoDescanso = prefs.getInt("restTime", 0)
            val tempoLuta = prefs.getInt("uptime", 0)

            binding.tietValueTimeForRest.setText(tempoDescanso.toString())
            binding.tietValueAverageFightTime.setText(tempoLuta.toString())
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao carregar dados: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}