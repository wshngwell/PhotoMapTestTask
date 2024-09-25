package com.example.map.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.map.databinding.FragmentRegisterBinding
import com.example.map.presentation.viewmodels.RegistrationViewModel
import com.example.map.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject


class RegisterFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentRegisterBinding

    private val registerViewModel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[RegistrationViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.button.setOnClickListener {
            registerViewModel.register(
                binding.loginEt.text.toString(),
                binding.passwordEtFirst.text.toString(),
                binding.passwordEtSecond.text.toString()
            )
        }

        registerViewModel.accessTokenLiveData.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = View.INVISIBLE
            when (state) {
                is AuthorizationState.Success -> {
                    context?.let {
                        val intent =
                            NavigationActivity.newIntent(
                                it,
                                state.registrationUserResult.login,
                                state.registrationUserResult.token
                            )
                        startActivity(intent)
                    }
                }

                is AuthorizationState.Error -> {
                    Toast.makeText(
                        context,
                        state.errorName,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                AuthorizationState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }

    }
}