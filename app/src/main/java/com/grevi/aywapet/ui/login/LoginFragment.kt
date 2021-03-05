package com.grevi.aywapet.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentLoginBinding
import com.grevi.aywapet.ui.home.HomeActivity
import com.grevi.aywapet.ui.viewmodel.RegisterViewModel
import com.grevi.aywapet.utils.Constant.RC_SIGN
import com.grevi.aywapet.utils.NetworkUtils
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val registerViewModel : RegisterViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var googleSignClient : GoogleSignInClient
    private lateinit var googleSignOptions : GoogleSignInOptions
    private lateinit var fbAuth : FirebaseAuth
    private val networkUtils: NetworkUtils by lazy { NetworkUtils(requireContext()) }

    private val TAG = LoginFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        observeNetwork()
        fbAuth = FirebaseAuth.getInstance()
    }

    private fun observeNetwork() = with(binding) {
        networkUtils.networkStatus.observe(viewLifecycleOwner) {
            if (it) {
                configureSignSetting()
                initView()
            }else {
                snackBar(root, getString(R.string.lost_network_text)).show()
            }
        }
    }

    private fun initView() {
        binding.btnLoginGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun configureSignSetting() {
        googleSignOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(requireContext(), googleSignOptions)
    }

    @Suppress("DEPRECATION")
    private fun signIn() {
        val signIntent = googleSignClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.v(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e : ApiException) {
                    Log.e(TAG, e.toString())
                }
            } else {
                snackBar(binding.root, task.exception.toString()).show()
                Log.e(TAG, task.exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        fbAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.user?.let { users ->
                        observeViewModel(users.email!!, users.displayName!!, users.uid)
                        binding.btnLoginGoogle.isEnabled = false
                    }
                } else {
                    snackBar(binding.root, "Gagal Login").show()
                    binding.btnLoginGoogle.isEnabled = true
                }
            }
    }

    private fun observeViewModel(email : String, name : String, uid : String) {
        registerViewModel.getEmail(email).observe(viewLifecycleOwner, { state ->
            when(state) {
                is State.Loading -> binding.btnLoginGoogle.isEnabled = false
                is State.Error -> {
                    binding.btnLoginGoogle.isEnabled = true
                    snackBar(binding.root, state.exception.toString()).show()
                }
                is State.Success -> {
                    state.data.status.let { status ->
                        if (status) {
                            Intent(requireActivity(), HomeActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(this)
                            }
                        } else {
                            val action = LoginFragmentDirections.actionLoginFragmentToRegisFragment(name, email, uid)
                            navController.navigate(action)
                        }
                    }
                }
                else -> print("")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        registerViewModel.loginState.observe(viewLifecycleOwner, { state ->
            if (state) {
                Intent(requireActivity(), HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(this)
                    activity?.finish()
                }
            }
        })
    }
}