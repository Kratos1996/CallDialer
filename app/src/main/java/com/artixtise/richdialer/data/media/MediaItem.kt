package com.artixtise.richdialer.data.media

data class MediaItem(
        var id : Long,
        var mMediaUri: String,
        var mMediaType: String,
        var mMediaDate: Long,
        var absolutePath : String,
        var duration : Long,
        var title : String
)
/**
 * MediaType
 * IMAGE for images
 * VIDEO for videos
 * */