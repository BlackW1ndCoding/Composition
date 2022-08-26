package ua.blackwindstudio.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ua.blackwindstudio.ui.R
import ua.blackwindstudio.ui.databinding.FragmentWelcomeBinding
import ua.blackwindstudio.ui.utils.autoCleared

class WelcomeFragment: Fragment(R.layout.fragment_welcome) {
    private var binding by autoCleared<FragmentWelcomeBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWelcomeBinding.bind(view)

        binding.buttonAccept.setOnClickListener {

        }
    }
}