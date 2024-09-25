package com.example.map.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.map.databinding.FragmentLoginBinding
import com.example.map.presentation.viewmodels.LoginViewModel
import com.example.map.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val loginViewModel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[LoginViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            loginViewModel.login(
                binding.loginEt.text.toString(),
                binding.passwordEtFirst.text.toString()
            )
        }

        loginViewModel.accessTokenLiveData.observe(viewLifecycleOwner) { state ->
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