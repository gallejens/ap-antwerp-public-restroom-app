package edu.ap.mobile_development_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.mobile_development_project.entities.Toilet

class ListViewAdapter(private var toiletList: List<Toilet>) : RecyclerView.Adapter<ListViewAdapter.ViewHolder>() {

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var title : TextView

        init {
            title = itemView.findViewById(R.id.title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.listitem_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = toiletList[position]
        holder.title.text = t.address + " - ${t.distance}km"
    }

    override fun getItemCount(): Int {
        return toiletList.size
    }
}