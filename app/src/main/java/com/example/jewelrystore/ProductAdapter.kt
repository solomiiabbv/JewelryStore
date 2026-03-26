package com.example.jewelrystore

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct) // <- важливо
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

        // Встановлюємо фото: з галереї/камери чи з drawable
        product.imageUri?.let {
            holder.ivProduct.setImageURI(it)
        } ?: product.imageResId?.let {
            holder.ivProduct.setImageResource(it)
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = products.size
}