package com.asifshaikh.firebaseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.asifshaikh.firebaseproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        binding.btnRegister.setOnClickListener {
            getResgister()
        }

        binding.btnLogin.setOnClickListener {
            getLogin()
        }

    }

    override fun onStart() {
        super.onStart()
        checkLoggedInstance()
    }

    private fun checkLoggedInstance() {
        if (auth.currentUser == null) {
            binding.tvLoggedIn.text = "Your are not logged in"
        } else {
            binding.tvLoggedIn.text = "You are logged in"
        }
    }

    private fun getResgister() {
        val email = binding.etEmailRegister.text.toString()
        val passwrod = binding.etPasswordRegister.text.toString()

        if (email.isNotEmpty() && passwrod.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, passwrod).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInstance()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun getLogin() {
        val email = binding.etEmailLogin.text.toString()
        val passwrod = binding.etPasswordLogin.text.toString()

        if (email.isNotEmpty() && passwrod.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, passwrod).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInstance()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}