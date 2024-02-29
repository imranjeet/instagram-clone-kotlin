package com.vihaan.instagramclone

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.databinding.ActivitySignUpBinding
import com.vihaan.instagramclone.utils.USERS_NODE
import com.vihaan.instagramclone.utils.USERS_PROFILE_FOLDER
import com.vihaan.instagramclone.utils.uploadImage

class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
             uploadImage(uri, USERS_PROFILE_FOLDER){
                 if(it==null){

                 } else {
                     user.image = it
                     binding.profileImage.setImageURI(uri)
                 }
             }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val text = "<font color=#FFFFFFFF>Already have an Account</font> <font color=#1E88E5>Login</font>"
        binding.login.setText(Html.fromHtml(text))
        user = User()
        if(intent.hasExtra("MODE")){
            if (intent.getIntExtra("MODE", -1) == 1){
                binding.signUpBin.text = "Update Profile"
                Firebase.firestore.collection(USERS_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                    user=it.toObject<User>()!!
                    if (!user.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.profileImage)
                    }
                    binding.name.editText?.setText(user.name)
                    binding.email.editText?.setText(user.email)
                    binding.password.editText?.setText(user.password)
                    binding.login.visibility = View.GONE

                }
            }
        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
        }
        binding.signUpBin.setOnClickListener {
            if(intent.hasExtra("MODE")){
                if (intent.getIntExtra("MODE", -1) == 1){
                    Firebase.firestore.collection(USERS_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
                            finish()
                        }
                }
            }
            else {
                if (binding.name.editText?.text.toString().equals("")
                    or binding.email.editText?.text.toString().equals("")
                    or binding.password.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please fill the all the feild",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.name.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            Firebase.firestore.collection(USERS_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
                                    finish()
                                }
                        } else {
                            Log.d("LOGIN", "Your message here ${result.exception}")
                            Toast.makeText(
                                this@SignUpActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }
}