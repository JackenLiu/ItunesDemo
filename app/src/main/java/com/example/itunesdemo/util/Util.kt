package com.example.itunesdemo.util

import com.example.itunesdemo.MainActivity
import com.example.itunesdemo.R

object Util {
    fun getKindStr(activity: MainActivity, kind: String?) = when (kind) {
        "feature-movie" -> activity.resources.getString(R.string.movie)
        "book" -> activity.resources.getString(R.string.book)
        "song" -> activity.resources.getString(R.string.song)
        "album" -> activity.resources.getString(R.string.album)
        "coached-audio" -> activity.resources.getString(R.string.coached_audio)
        "interactive-booklet" -> activity.resources.getString(R.string.interactive_booklet)
        "music-video" -> activity.resources.getString(R.string.music_video)
        "pdf podcast" -> activity.resources.getString(R.string.pdf_podcast)
        "podcast-episode" -> activity.resources.getString(R.string.podcast_episode)
        "software-package" -> activity.resources.getString(R.string.software_package)
        "tv-episode" -> activity.resources.getString(R.string.tv_episode)
        "artist" -> activity.resources.getString(R.string.artist)
        "USA" -> activity.resources.getString(R.string.USA)
        else -> activity.resources.getString(R.string.other)
    }

}