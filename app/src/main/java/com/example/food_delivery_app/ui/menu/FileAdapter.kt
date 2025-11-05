package com.example.food_delivery_app.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_delivery_app.data.model.FileItem
import com.example.food_delivery_app.databinding.ItemFileBinding
import java.text.DateFormat
import java.util.Date
import kotlin.math.log10
import kotlin.math.pow

class FileAdapter(
    private val onItemClick: (FileItem) -> Unit
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private val items = mutableListOf<FileItem>()

    fun submitList(newItems: List<FileItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ItemFileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FileViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class FileViewHolder(
        private val binding: ItemFileBinding,
        private val onClick: (FileItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FileItem) = with(binding) {
            textName.text = item.displayName
            textMime.text = item.mimeType ?: "unknown"
            textSize.text = formatSize(item.sizeBytes)
            textDate.text = DateFormat.getDateTimeInstance()
                .format(Date(item.dateModifiedMillis))

            Glide.with(imageThumb.context)
                .load(item.uri)               // content:// Uri
                .thumbnail(0.25f)
                .into(imageThumb)

            root.setOnClickListener { onClick(item) }
        }

        private fun formatSize(bytes: Long): String {
            if (bytes <= 0) return "0 B"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val group = (log10(bytes.toDouble()) / log10(1024.0)).toInt().coerceIn(0, units.lastIndex)
            val value = bytes / 1024.0.pow(group.toDouble())
            return String.format("%.1f %s", value, units[group])
        }
    }
}