package com.example.submissionintermediate1.ui.loginregist.login

import android.content.Intent
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
import com.example.submissionintermediate1.databinding.FragmentLoginBinding
import com.example.submissionintermediate1.ui.ViewModelFactory
import com.example.submissionintermediate1.ui.main.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels { factory }

    private var job: Job = Job()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        factory = ViewModelFactory.getInstance(requireActivity())
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.apply {
            binding.apply {
                edLoginEmail.addTextChangedListener (object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        enableDisableButton()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
                edLoginPassword.addTextChangedListener (object: TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        enableDisableButton()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
                btnLogin.setOnClickListener{
                    login()
                }
            }
        }
        return binding.root
    }

    private fun login() {
        binding.apply {
            val password = edLoginPassword.text.toString().trim()
            val email = edLoginEmail.text.toString().trim()
            showLoading(true)
            lifecycleScope.launchWhenResumed {
                if (job.isActive) job.cancel()
                job = launch {
                    viewModel.login(email, password).collect { result ->
                        result.onSuccess {
                            it.result.token.let { token ->
                                viewModel.saveToken(token)
                                val intent = Intent(requireActivity(), MainActivity::class.java)
                                intent.putExtra(MainActivity.EXTRA_TOKEN, token)
                                startActivity(intent)
                                showLoading(false)
                            }
                        }
                        result.onFailure {
                            toastMsg(getString(R.string.login_failed))
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun enableDisableButton(){
        binding.apply {
            val password = edLoginPassword.text
            val email = edLoginEmail.text
            btnLogin.isEnabled = password.toString().length >= 6 && Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
        }
    }

    private fun toastMsg(msg: String){
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean){
        binding.btnLogin.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}