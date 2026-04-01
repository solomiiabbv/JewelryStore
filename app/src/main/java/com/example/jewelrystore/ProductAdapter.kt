package com.example.jewelrystore

import android.app.AlertDialog
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: MutableList<Product>,
    private val onClick: (Product) -> Unit,
    private val onEditClick: (Product, Int) -> Unit,
    private val onDeleteClick: (Product, Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivImage)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.tvName.text = product.name
        holder.tvPrice.text = product.price

        // ЗМІНА ТУТ: використовуємо setImageResource для Int (ID з drawable)
        holder.ivImage.setImageResource(product.imageResId)

        holder.itemView.setOnClickListener { onClick(product) }
        holder.btnEdit.setOnClickListener { onEditClick(product, position) }

        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Видалити товар?")
                .setMessage("Ви впевнені?")
                .setPositiveButton("Так") { _, _ -> onDeleteClick(product, position) }
                .setNegativeButton("Ні", null)
                .show()
        }
    }

    override fun getItemCount() = products.size
}