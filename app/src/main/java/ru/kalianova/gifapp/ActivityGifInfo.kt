package ru.kalianova.gifapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import ru.kalianova.gifapp.databinding.ActivityGifInfoBinding

private lateinit var binding: ActivityGifInfoBinding

class ActivityGifInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGifInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        val username = intent.getStringExtra("username")
        var date = intent.getStringExtra("date")
        date = "${date?.substring(8, 10)}.${date?.substring(5, 7)}.${date?.substring(0, 4)}"

        binding.textViewGifInfoTitle.text = title
        if (username != "")
            binding.textViewGifInfoUsername.text = "Ник автора: $username"
        else
            binding.textViewGifInfoUsername.visibility = View.GONE
        binding.textViewGifInfoDate.text = "Дата загрузки: $date"

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(this).load(url).placeholder(circularProgressDrawable)
            .into(binding.imageViewGifInfo)

        //Копирование ссылки на gif в буфер обмена при нажатии на картинку
        binding.imageViewGifInfo.setOnClickListener {
            val clip: ClipData = ClipData.newPlainText("url", url)
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Ссылка на gif-изображение скопирована", Toast.LENGTH_SHORT).show()
        }
    }
}