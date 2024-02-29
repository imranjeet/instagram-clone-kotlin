package com.vihaan.instagramclone.Post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vihaan.instagramclone.HomeActivity
import com.vihaan.instagramclone.Models.Post
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.databinding.ActivityPostBinding
import com.vihaan.instagramclone.utils.POST
import com.vihaan.instagramclone.utils.POST_FOLDER
import com.vihaan.instagramclone.utils.USERS_NODE
import com.vihaan.instagramclone.utils.USERS_PROFILE_FOLDER
import com.vihaan.instagramclone.utils.uploadImage

class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl:String?=null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let {
            uploadImage(uri, POST_FOLDER){
                url->
                if(url!=null){
                    binding.selectImage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }
        binding.postButton.setOnClickListener {
//            Firebase.firestore.collection(USERS_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
//                var user=it.toObject<User>()
//
//            }
            val post:Post=Post(postUrl=imageUrl!!, caption= binding.caption.editText?.text.toString(), uid= Firebase.auth.currentUser!!.uid.toString(), time= System.currentTimeMillis().toString())
            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post).addOnSuccessListener {
                    startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                    finish()
                }
            }

        }
    }
}