package com.bluecode.ct037.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluecode.ct037.R
import com.bluecode.ct037.databinding.FragmentHomeBinding
import java.util.concurrent.TimeUnit

enum class Button(val value: Int) {
    BtnStart(0),
    BtnPause(1),
    BtnReset(2)
}

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? =null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 5 * 60 * 1000L
    private val totalTimeInMillies = 5 * 60 * 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            defaultCountdown()
            disabledButtons(Button.BtnStart.value,true)
            disabledButtons(Button.BtnPause.value,false)
            btnOpenSettings.setOnClickListener{
                parentFragmentManager.beginTransaction().replace(R.id.home, SettingsActivityFragment()).addToBackStack(null).commit()
            }

            llBtnHelpers.getChildAt(Button.BtnStart.value).setOnClickListener{
                if(!isTimerRunning){
                    startCountdown()
                    disabledButtons(Button.BtnStart.value,false)
                    disabledButtons(Button.BtnPause.value,true)
                }
            }

            llBtnHelpers.getChildAt(Button.BtnPause.value).setOnClickListener{
                if(isTimerRunning){
                    stopCountdown()
                    disabledButtons(Button.BtnPause.value,false)
                    disabledButtons(Button.BtnStart.value,true)
                }
            }

            llBtnHelpers.getChildAt(Button.BtnReset.value).setOnClickListener{
                resetCountdown()
                disabledButtons(Button.BtnStart.value,true)
                disabledButtons(Button.BtnPause.value,false)
            }
        }
    }

    private fun disabledButtons(indexBtn: Int, status: Boolean) {
            binding.llBtnHelpers.getChildAt(indexBtn).isEnabled = status
            binding.llBtnHelpers.getChildAt(indexBtn).isClickable = status
    }

    private fun stopCountdown() {
        isTimerRunning = false
        countDownTimer?.cancel()
    }

    private fun resetCountdown() {
        stopCountdown()
        defaultCountdown()
    }

    private fun defaultCountdown(){
        val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillies)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMillies) % 60
        binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun startCountdown() {
        val startTime = if(timeLeftInMillis > 0) timeLeftInMillis else totalTimeInMillies
        countDownTimer = object : CountDownTimer(startTime,1000) {
            override fun onTick(millisUntilFinished:Long) {
                timeLeftInMillis = millisUntilFinished // Atualiza o tempo restante
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                binding.tvClockText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvClockText.text = "00:00"
                isTimerRunning = false
            }
        }.start()

        isTimerRunning = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()

        _binding = null
    }

}
