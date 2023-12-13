package com.example.pizzaapp

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.model.MenuModel

class MakananAdapter (private val list: ArrayList<MenuModel>):
    RecyclerView.Adapter<MakananAdapter.MakananViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MakananAdapter.MakananViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.cardview_makanan, parent, false)

        return MakananViewHolder((cellForRow))
    }

    override fun onBindViewHolder(holder: MakananAdapter.MakananViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MakananViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val textId : TextView
        val textNama : TextView
        val textHarga : TextView
        val imageMenu : ImageView
        val buttonedit : Button
        val buttondelete : Button
        val context = v.context

        init {
            textId = v.findViewById(R.id.textIdMakanan)
            textNama = v.findViewById(R.id.textNamaMakanan)
            textHarga = v.findViewById(R.id.textHargaMakanan)
            imageMenu = v.findViewById(R.id.imageMakanan)
            buttonedit = v.findViewById(R.id.buttonEditMakanan)
            buttondelete = v.findViewById(R.id.buttonHapusMakanan)
            //event
            buttonedit.setOnClickListener {
                EditMenuActivity.idMakanan = textId.text.toString().toInt()
                EditMenuActivity.namaMakanan = textNama.text.toString()
                EditMenuActivity.hargamakanan = textHarga.text.toString().toInt()
                EditMenuActivity.gambarMakanan = imageMenu.drawable.toBitmap()
                val intent = Intent(context, EditMenuActivity::class.java)
                context.startActivity(intent)
            }

            buttondelete.setOnClickListener {
                val dbHelper = DatabaseHelper(context)
                dbHelper.deleteMenu(textId.text.toString().toInt())
                val intent =Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }

        fun bind(data: MenuModel) {
            val id:Int = data.id
            val nama:String = data.name
            val harga:Int = data.price
            val gambar:Bitmap = data.image

            textId.text = id.toString()
            textNama.text = nama
            textHarga.text = harga.toString()
            imageMenu.setImageBitmap(gambar)
        }
    }
}