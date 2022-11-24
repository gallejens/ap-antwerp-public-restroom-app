package edu.ap.mobile_development_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.mobile_development_project.entities.Toilet

class ListViewAdapter(private var toiletList: List<Toilet>) : RecyclerView.Adapter<ListViewAdapter.ViewHolder>() {

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var temp : TextView

        init {
            temp = itemView.findViewById(R.id.temp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.listitem_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.temp.text = toiletList[position].address
    }

    override fun getItemCount(): Int {
        return toiletList.size;
    }

}