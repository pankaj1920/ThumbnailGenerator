package android.generate.thumbnail

import android.app.Activity
import android.content.Intent
import android.generate.thumbnail.utils.BaseAdapter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    lateinit var image: ImageView
    lateinit var tvPleasWait: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn)



        image = findViewById<ImageView>(R.id.image)

        btn.setOnClickListener {
            val intent = Intent()
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select video"
                ), 8080
            )
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("requestCode $requestCode onActivityResult $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                8080 -> {
                    data?.let {

                        if (it.clipData != null) {
                            for (i in 0 until it.clipData!!.itemCount) {
                                val uri = it.clipData!!.getItemAt(i).uri
                                println("Video URI: $uri")
                                initRecyclerView(uri)
                            }
                        } else if (it.data != null) {
                            val uri = it.data
                            initRecyclerView(uri!!)
                            println("Video URI: $uri")
                        }
                    }
                }
            }
        } else {
            println("Failed to select video. Result code: $resultCode")
        }
    }

    private fun initRecyclerView(uri: Uri) {
            println("initRecyclerView ================== ")
            val thumbnailList = generateThumbnail(uri, this@MainActivity, 800, 15)
            println("thumbnailList ================== ")
            val recyclerView = findViewById<RecyclerView>(R.id.rvThumbnail)

            val adapter = BaseAdapter<Bitmap>().apply {

                setListOfItems(thumbnailList)

                setHolderBinding(object : BaseAdapter.HolderBinding<Bitmap> {
                    override fun bind(item: Bitmap, view: View, position: Int) {
                        val imageView = view.findViewById<ImageView>(R.id.thumbNail)
                        val tvCount = view.findViewById<TextView>(R.id.tvCount)
                        imageView.setImageBitmap(item)
                        tvCount.setText(position.toString())

                        imageView.setOnClickListener {
                            image.setImageBitmap(item)
                        }

                    }
                })

                setViewHolder(object : BaseAdapter.ViewHolder {
                    override fun create(parent: ViewGroup): View {
                        return LayoutInflater.from(parent.context)
                            .inflate(R.layout.thumbnail_item, parent, false)
                    }
                })

            }
            recyclerView.adapter = adapter
        }

}
