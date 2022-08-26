package ua.blackwindstudio.ui.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.databinding.FragmentGameBinding
import ua.blackwindstudio.ui.utils.autoCleared

class FragmentGame: Fragment(R.layout.fragment_game) {
    private var binding by autoCleared<FragmentGameBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGameBinding.bind(view)

        binding.textSum.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragmentGame_to_resultFragment
            )
        }
    }
}