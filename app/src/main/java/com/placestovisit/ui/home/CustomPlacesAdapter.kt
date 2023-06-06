package com.placestovisit.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.placestovisit.R
import com.placestovisit.firebase.repositories.FirebaseDBRepository
import com.placestovisit.models.Place

class CustomPlacesAdapter(private val context : Context)
    : ArrayAdapter<Place>(context,R.layout.place_list_item) {

    private var places = mutableListOf<Place>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = LayoutInflater.from(context).inflate(R.layout.place_list_item,null,true)

        val txtTitle = rootView.findViewById<TextView>(R.id.txtTitle)
        val txtCity = rootView.findViewById<TextView>(R.id.txtCity)
        val txtDescription = rootView.findViewById<TextView>(R.id.txtDescription)
        val btnHideDescImage = rootView.findViewById<ImageButton>(R.id.btnHideDescImage)
        val btnShowDescImage = rootView.findViewById<ImageButton>(R.id.btnShowDescImage)

        val place = places[position]

        val isExpandable : Boolean = place.isExpandable

        txtDescription.visibility = if(isExpandable) View.VISIBLE else View.GONE

        btnShowDescImage.setOnClickListener{
            place.isExpandable = true
            txtDescription.visibility = View.VISIBLE
            btnShowDescImage.visibility = View.GONE
            btnHideDescImage.visibility = View.VISIBLE
        }
        btnHideDescImage.setOnClickListener{
            place.isExpandable = false
            txtDescription.visibility = View.GONE
            btnShowDescImage.visibility = View.VISIBLE
            btnHideDescImage.visibility = View.GONE
        }

        txtTitle.text = place.title
        txtCity.text = place.city
        txtDescription.text = place.description

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