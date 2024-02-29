package com.vihaan.instagramclone.fregments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import com.vihaan.instagramclone.Models.Post
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.R
import com.vihaan.instagramclone.adapters.FollowAdapter
import com.vihaan.instagramclone.adapters.PostAdapter
import com.vihaan.instagramclone.databinding.FragmentHomeBinding
import com.vihaan.instagramclone.utils.FOLLOW
import com.vihaan.instagramclone.utils.POST
import com.vihaan.instagramclone.utils.USERS_NODE

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var postList = ArrayList<Post>()
    private var followList = ArrayList<User>()
    private lateinit var adapter: PostAdapter
    private lateinit var followAdapter: FollowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        adapter= PostAdapter(requireContext(), postList)
        binding.postRv.layoutManager=LinearLayoutManager(requireContext())
        binding.postRv.adapter=adapter

        followAdapter= FollowAdapter(requireContext(), followList)
        binding.followRv.layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.followRv.adapter=followAdapter
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList = ArrayList<Post>()
            postList.clear()
            for(i in it.documents){
                var post:Post = i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
            .get().addOnSuccessListener {
            var tempList=ArrayList<User>()
            followList.clear()
            for (i in it.documents){
                var user:User = i.toObject<User>()!!
                tempList.add(user)
            }
            followList.addAll(tempList)
            followAdapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USERS_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            val user:User=it.toObject<User>()!!
            if (!user.image.isNullOrEmpty()){
                Picasso.get().load(user.image).into(binding.profileImg)
            }
        }
    }
}