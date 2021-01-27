package com.grevi.aywapet.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.grevi.aywapet.ui.viewmodel.MainViewModel
import com.grevi.aywapet.utils.Constant.RC_SIGN
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.SharedUtils
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var googleSignClient : GoogleSignInClient
    private lateinit var googleSignOptions : GoogleSignInOptions
    private lateinit var fbAuth : FirebaseAuth

    private val TAG = LoginFragment::class.java.simpleName

    @Inject lateinit var sharedUtils: SharedUtils

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
        configureSignSetting()
        initView()
        fbAuth = FirebaseAuth.getInstance()
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

    private fun signIn() {
        val signIntent = googleSignClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN)
    }

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
                Log.e("TASK_EXCEPTION", task.exception.toString())
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
                    }
                } else {
                    snackBar(binding.root, "Gagal Login").show()
                }
            }
    }

    private fun observeViewModel(email : String, name : String, uid : String) {
        mainViewModel.getEmailVerify(email).observe(viewLifecycleOwner, { response ->
            when(response.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, response.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    response.data?.status?.let {
                        if (it) {
                            sharedUtils.setLoginKey()
                            response.data.result.let { users ->
                                val token = response.data.token
                                sharedUtils.setUserKey(users.email)
                                sharedUtils.setUniqueKey(users.id)
                                //mainViewModel.insertUser(users.id, users.username, users.email, users.name, users.uid, token)
                            }
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
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (sharedUtils.getLoginShared()) {
            Intent(requireActivity(), HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(this)
                activity?.finish()
            }
        }
    }
}