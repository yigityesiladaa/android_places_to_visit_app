package com.placestovisit.ui.home

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.placestovisit.R
import com.placestovisit.models.Place

class CustomPlacesAdapter(private val context : Context)
    : ArrayAdapter<Place>(context,R.layout.place_list_item) {

    private var places = mutableListOf<Place>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = LayoutInflater.from(context).inflate(R.layout.place_list_item,null,true)

        val txtTitle = rootView.findViewById<TextView>(R.id.txtTitle)
        val txtCity = rootView.findViewById<TextView>(R.id.txtCity)
        val txtDescription = rootView.findViewById<TextView>(R.id.txtDescription)
        val showLessLayout = rootView.findViewById<ConstraintLayout>(R.id.showLessLayout)
        val btnShowDescImage = rootView.findViewById<ImageButton>(R.id.btnShowDescImage)
        val imgPlaceImage = rootView.findViewById<ImageView>(R.id.imgPlaceImage)

        val place = places[position]

        val isExpandable : Boolean = place.isExpandable

        txtDescription.visibility = if(isExpandable) View.VISIBLE else View.GONE

        btnShowDescImage.setOnClickListener{
            place.isExpandable = true
            txtDescription.visibility = View.VISIBLE
            btnShowDescImage.visibility = View.GONE
            showLessLayout.visibility = View.VISIBLE
            imgPlaceImage.visibility = View.VISIBLE
        }
        showLessLayout.setOnClickListener{
            place.isExpandable = false
            txtDescription.visibility = View.GONE
            btnShowDescImage.visibility = View.VISIBLE
            showLessLayout.visibility = View.GONE
            imgPlaceImage.visibility = View.GONE
        }

        txtTitle.text = place.title
        txtCity.text = place.city
        txtDescription.text = place.description
        Glide.with(context).load(place.imageUrl).into(imgPlaceImage)

        return rootView
    }

    override fun getCount(): Int {
        return places.count()
    }

    fun submitList(list : MutableList<Place>){
        places = list
        notifyDataSetChanged()
    }
}