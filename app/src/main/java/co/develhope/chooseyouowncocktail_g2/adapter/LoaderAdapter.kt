package co.develhope.chooseyouowncocktail_g2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.databinding.LoadingRingBinding


class LoaderAdapter : RecyclerView.Adapter<LoaderAdapter.LoaderViewHolder>() {

    lateinit var holder: LoaderViewHolder

    inner class LoaderViewHolder(val binding: LoadingRingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LoaderAdapter.LoaderViewHolder {

        val binding = LoadingRingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoaderViewHolder(binding)
    }

    override fun onBindViewHolder(loaderholder: LoaderViewHolder, position: Int) {
        holder = loaderholder
    }


    fun updateLoadingRing(isLoading: Boolean) {
        if (this::holder.isInitialized) {
            if (isLoading) {
                holder.binding.loadingRing.visibility =
                    View.VISIBLE
            } else {
                holder.binding.loadingRing.visibility = View.INVISIBLE

            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}
