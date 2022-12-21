package com.example.submissionintermediate1.ui.loginregist.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.R
import com.example.submissionintermediate1.databinding.FragmentRegisterBinding
import com.example.submissionintermediate1.ui.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: RegisterViewModel by viewModels { factory }

    private var job: Job = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        factory = ViewModelFactory.getInstance(requireActivity())
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.apply {
            btnSignUp.setOnClickListener{
                register()
            }
            edSignUpEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    enableDisableButton()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            edSignUpUsername.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    enableDisableButton()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            edSignUpPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    enableDisableButton()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
        return binding.root
    }
    private fun register() {
        val name = binding.edSignUpUsername.text.toString().trim()
        val email = binding.edSignUpEmail.text.toString().trim()
        val password = binding.edSignUpPassword.text.toString().trim()
        showLoading(true)
        lifecycleScope.launchWhenResumed {
            if (job.isActive) job.cancel()
            job = launch {
                viewModel.register(name, email, password).collect { result ->
                    result.onSuccess {
                        toastMsg(getString(R.string.signup_success))
                        showLoading(false)
                    }
                    result.onFailure {
                        toastMsg(getString(R.string.signup_error))
                        showLoading(false)
                    }
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.btnSignUp.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun enableDisableButton(){
        binding.apply {
            val username = edSignUpUsername.text
            val email = edSignUpEmail.text
            val password = edSignUpPassword.text
            btnSignUp.isEnabled = password.toString().length >= 6 && username.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
        }
    }
    private fun toastMsg(msg: String){
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }
}