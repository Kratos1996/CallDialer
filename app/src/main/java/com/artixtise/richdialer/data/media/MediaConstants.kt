package com.artixtise.richdialer.data.media

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object MediaConstants {

    private var IMAGE_VIDEO_PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.PARENT,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Video.Media.DURATION,
        MediaStore.Files.FileColumns.DATA,
    )

    var IMAGE_VIDEO_URI: Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        else MediaStore.Files.getContentUri("external")


    private var SELECTION_IMAGE_VIDEO = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

    private var SELECTION_IMAGE = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

    private var SELECTION_VIDEO = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

    private var IMAGE_VIDEO_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC"

    fun getImageVideoCursor(
        context: Context
    ): Cursor? {
        return context.contentResolver
            .query(
                IMAGE_VIDEO_URI,
                IMAGE_VIDEO_PROJECTION,
                SELECTION_IMAGE_VIDEO,
                null,
                IMAGE_VIDEO_ORDER_BY
            )
    }

    fun getImageCursor(context: Context): Cursor? {
        return context.contentResolver
            .query(
                IMAGE_VIDEO_URI,
                IMAGE_VIDEO_PROJECTION,
                SELECTION_IMAGE,
                null,
                IMAGE_VIDEO_ORDER_BY
            )
    }

    fun getCursorByFolder(bucketPath: String, context: Context): Cursor? {
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?"
        return context.contentResolver.query(
            IMAGE_VIDEO_URI,
            IMAGE_VIDEO_PROJECTION,
            selection,
            arrayOf(bucketPath),
            IMAGE_VIDEO_ORDER_BY
        )
    }

//    fun getFolders(mContext: Context): List<MediaFolder> {
//        val mediaFolders: MutableList<MediaFolder> = ArrayList()
//        val foldersName: MutableList<String> = ArrayList()
//        val projection =
//            arrayOf(
//                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
//                MediaStore.Files.FileColumns.DATA
//            )
//        val cursor = mContext.contentResolver.query(
//            IMAGE_VIDEO_URI,
//            projection,
//            null,
//            null,
//            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + " DESC"
//        )
//        if (cursor != null) {
//            var file: File
//            while (cursor.moveToNext()) {
//                var bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]))
//                val firstImage = cursor.getString(cursor.getColumnIndex(projection[1]))
//                file = File(firstImage)
//                if (bucketPath != null){
//                if (file.exists() and !foldersName.contains(bucketPath)) {
//                    mediaFolders.add(MediaFolder(bucketPath, firstImage))
//                    foldersName.add(bucketPath)
//                }
//                }
//            }
//            cursor.close()
//        }
//        return mediaFolders
//    }

    fun getVideoCursor(
        context: Context
    ): Cursor? {
        return context.contentResolver
            .query(
                IMAGE_VIDEO_URI,
                IMAGE_VIDEO_PROJECTION,
                SELECTION_VIDEO,
                null,
                IMAGE_VIDEO_ORDER_BY
            )
    }
}