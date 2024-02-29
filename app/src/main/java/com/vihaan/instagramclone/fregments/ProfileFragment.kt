package com.vihaan.instagramclone.fregments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.SignUpActivity
import com.vihaan.instagramclone.adapters.ViewPagerAdapter
import com.vihaan.instagramclone.databinding.FragmentProfileBinding
import com.vihaan.instagramclone.utils.USERS_NODE

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity,SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
//            activity?.finish()
        }
        viewPagerAdapter= ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(), "My Posts")
        viewPagerAdapter.addFragments(MyReelsFragment(), "My Reels")
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        return binding.root
    }
    companion object {
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USERS_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val user:User=it.toObject<User>()!!
            binding.name.text = user.name
            binding.bio.text = user.email
            if (!user.image.isNullOrEmpty()){
                Picasso.get().load(user.image).into(binding.profileImage)
            }
        }
    }
}