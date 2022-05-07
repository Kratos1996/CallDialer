package com.artixtise.richdialer.aws

/**
 * Created by Ishant Sharma
 */
interface FileUploadListener {
    fun onUploadSuccess()
    fun onUploadFailed(msg: String?)
    fun onProgress(percent: Int)
}