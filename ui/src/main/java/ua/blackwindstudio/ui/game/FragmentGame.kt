package ua.blackwindstudio.ui.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.GameResultArg
import ua.blackwindstudio.ui.args.GameSettingsArg
import ua.blackwindstudio.ui.databinding.FragmentGameBinding
import ua.blackwindstudio.ui.utils.autoCleared

class FragmentGame: Fragment(R.layout.fragment_game) {
    private var binding by autoCleared<FragmentGameBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGameBinding.bind(view)

        binding.textSum.setOnClickListener {
            val action = FragmentGameDirections.actionFragmentGameToResultFragment(
                GameResultArg(
                    false, 2, 2, GameSettingsArg(10, 3, 5, 30)
                )
            )
            findNavController().navigate(
                action
            )
        }
    }
}