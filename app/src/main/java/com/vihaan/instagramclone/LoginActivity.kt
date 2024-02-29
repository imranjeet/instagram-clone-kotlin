package com.vihaan.instagramclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.databinding.ActivityLoginBinding
import com.vihaan.instagramclone.databinding.ActivitySignUpBinding
import com.vihaan.instagramclone.utils.USERS_NODE

class LoginActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            if (binding.email.editText?.text.toString().equals("")
                or binding.password.editText?.text.toString().equals("")
            ) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please fill the all the feild",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var user = User(binding.email.editText?.text.toString(),
                    binding.password.editText?.text.toString())
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            it.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }
    }

}