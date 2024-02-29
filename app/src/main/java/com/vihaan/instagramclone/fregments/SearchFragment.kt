package com.vihaan.instagramclone.fregments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.adapters.SearchAdapter
import com.vihaan.instagramclone.databinding.FragmentSearchBinding
import com.vihaan.instagramclone.utils.USERS_NODE

class SearchFragment : Fragment() {
   lateinit var binding : FragmentSearchBinding
   lateinit var adapter:SearchAdapter
   var userList=ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.rv.layoutManager=LinearLayoutManager(requireContext())
        adapter= SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter
        Firebase.firestore.collection(USERS_NODE).get().addOnSuccessListener {
            var tempList=ArrayList<User>()
            userList.clear()
            for (i in it.documents){
                if(i.id.toString() != (Firebase.auth.currentUser!!.uid)){
                    var user:User = i.toObject<User>()!!
                    tempList.add(user)
                }
            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        binding.searchButton.setOnClickListener {
            val searchText = binding.searchView.text.toString().trim()

            if (searchText.isNotEmpty()) {
                Firebase.firestore.collection(USERS_NODE)
                    .whereEqualTo("name", searchText)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        userList.clear()
                        for (document in querySnapshot.documents) {
                            val user = document.toObject<User>()
                            if (user != null && document.id != Firebase.auth.currentUser!!.uid) {
                                userList.add(user)
                            }
                        }
                        adapter.notifyDataSetChanged()

                        if (userList.isEmpty()) {
                            // Handle case when no users match the search query
                            // For example, display a toast message or update UI
                        }
                    }
            } else {
                // If search text is empty, display all users again
                Firebase.firestore.collection(USERS_NODE).get().addOnSuccessListener { querySnapshot ->
                    userList.clear()
                    for (document in querySnapshot.documents) {
                        val user = document.toObject<User>()
                        if (user != null && document.id != Firebase.auth.currentUser!!.uid) {
                            userList.add(user)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
        return binding.root
    }

    companion object {
    }
}