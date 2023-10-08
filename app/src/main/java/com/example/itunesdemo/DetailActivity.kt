package com.example.itunesdemo

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.SeekBar
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DetailActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_detail)

        iv_close.setOnClickListener { supportFinishAfterTransition() }
        tv_title.text = intent.getStringExtra("tv_title_key")
        tv_detail.text = intent.getStringExtra("tv_detail_key")


        val futureTarget = Glide.with(this)
            .asFile()
            .load(intent.getStringExtra("artworkUrl_key"))
            .submit(300, 300) // 你想要获取的缓存图片的宽和高

        GlobalScope.launch {
            try {
                val cacheFile: File = futureTarget.get() // 在子线程中执行
                // 你可以做一些事情，比如将文件转换为 Bitmap 等
                val bitmap = BitmapFactory.decodeFile(cacheFile.path)
                withContext(Dispatchers.Main) {
                    // 更新UI等
                    iv.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                // 处理异常
            } finally {
                Glide.with(this@DetailActivity).clear(futureTarget) // 清除你的目标，避免内存泄漏
            }
        }


        val url = intent.getStringExtra("music_url_key")
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                seekBar.max = duration
                updateSeekBar()
                btnPlayPause.isEnabled = true
                btnPlayPause.text = resources.getString(R.string.click_to_play)
            }
        }

        btnPlayPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                btnPlayPause.text = resources.getString(R.string.play)
            } else {
                mediaPlayer?.start()
                btnPlayPause.text = resources.getString(R.string.pause)
                updateSeekBar()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateSeekBar() {
        if (mediaPlayer == null) return
        seekBar.progress = mediaPlayer?.currentPosition ?: 0
        if (mediaPlayer?.isPlaying == true) {
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    updateSeekBar()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}