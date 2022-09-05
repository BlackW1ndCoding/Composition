package ua.blackwindstudio.ui.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.args.GameResultArg
import ua.blackwindstudio.ui.databinding.FragmentResultBinding
import ua.blackwindstudio.ui.utils.autoCleared

class ResultFragment: Fragment(R.layout.fragment_result) {
    private val args by navArgs<ResultFragmentArgs>()

    private var binding by autoCleared<FragmentResultBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResultBinding.bind(view)

        binding.renderViewValues(
            requireContext(),
            GameResultArg.mapToGameResult(args.gameResultArg)
        )

        binding.buttonTryAgain.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}