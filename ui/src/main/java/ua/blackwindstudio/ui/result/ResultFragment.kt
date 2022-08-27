package ua.blackwindstudio.ui.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.databinding.FragmentResultBinding
import ua.blackwindstudio.ui.utils.autoCleared

class ResultFragment: Fragment(R.layout.fragment_result) {
    private var binding by autoCleared<FragmentResultBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultBinding.bind(view)

        binding.buttonTryAgain.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_chooseDifficultyFragment)
        }
    }
}