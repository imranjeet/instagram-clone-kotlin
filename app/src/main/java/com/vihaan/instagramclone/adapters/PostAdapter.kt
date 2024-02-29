package com.vihaan.instagramclone.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vihaan.instagramclone.Models.Post
import com.vihaan.instagramclone.Models.User
import com.vihaan.instagramclone.R
import com.vihaan.instagramclone.databinding.PostRvBinding
import com.vihaan.instagramclone.utils.USERS_NODE

class PostAdapter(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    inner class MyHolder(var binding: PostRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            Firebase.firestore.collection(USERS_NODE).document(postList.get(position).uid).get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()
                    Glide.with(context).load(user!!.image).placeholder(R.drawable.profile).into(holder.binding.profileImage)
                    holder.binding.name.text = user.name
                }

        } catch (e:Exception) {

        }
        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.loading).into(holder.binding.postImage)
        try {
            val timeInMillis = postList[position].time.toLongOrNull()
            val txt = if (timeInMillis != null) {
                TimeAgo.using(timeInMillis)
            } else {
                // Handle invalid time format
                ""
            }
            holder.binding.time.text = txt
        } catch (e: NumberFormatException) {
            // Handle NumberFormatException
            holder.binding.time.text = ""
        }
        holder.binding.share.setOnClickListener {
            var i=Intent(Intent.ACTION_SEND)
            i.type="text/plain"
            i.putExtra(Intent.EXTRA_TEXT, postList.get(position).postUrl)
            context.startActivity(i)
        }
        holder.binding.cp.text = postList.get(position).caption
        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.liked)
        }
    }
}