package com.example.ghibli_quiz

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PeopleAdapter (val data: List<PeopleItem>,
                     val context: Activity,
                     val onItemClickListener: View.OnClickListener) : RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    class ViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {
        val age: TextView = rowView.findViewById(R.id.textViewAge)
        val image: ImageView = rowView.findViewById(R.id.imgViewGender)
        val name :  TextView = rowView.findViewById(R.id.textViewIdentity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.character_list_item,
            parent,
            false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(rowView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val people_info: PeopleItem = data[position]
        holder.age.text = people_info.age
        holder.name.text = people_info.name

        if (people_info.gender == "Male")
            holder.image.setImageResource(R.drawable.male)
        else
            holder.image.setImageResource(R.drawable.femele)
        holder.itemView.tag = position
    }
}