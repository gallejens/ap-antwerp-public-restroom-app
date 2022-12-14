package edu.ap.mobile_development_project

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.ap.mobile_development_project.entities.Toilet
import org.w3c.dom.Text

class ListViewAdapter(private var toiletList: List<Toilet>, private var onClick: (index: Int) -> Unit) : RecyclerView.Adapter<ListViewAdapter.ViewHolder>() {

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var title : TextView
        var description: TextView
        var openinghours: TextView

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            openinghours = itemView.findViewById(R.id.openinghours)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.listitem_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = toiletList[position]

        // Handle folding
        holder.itemView.setOnClickListener {
            val hiddenView = holder.itemView.findViewById<ConstraintLayout>(R.id.hidden_layout);

            if (hiddenView.visibility === View.VISIBLE) {
                hiddenView.visibility = View.GONE
            } else {
                hiddenView.visibility = View.VISIBLE
            }
        }

        // Show on map button
        holder.itemView.findViewById<Button>(R.id.showOnMapButton).setOnClickListener {
            onClick(position)
        }

        holder.title.text = t.address + " - ${t.distance}km"
        holder.description.text = "Beschrijving: ${t.description}"
        holder.openinghours.text = "Openingsuren: ${t.opening_hours ?: "Niet gekend"}"
    }

    override fun getItemCount(): Int {
        return toiletList.size
    }
}