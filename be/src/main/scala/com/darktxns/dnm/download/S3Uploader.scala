package com.darktxns.dnm.download

import java.io.File

import com.amazonaws.annotation.ThreadSafe
import com.amazonaws.event.{ProgressEvent, ProgressListener}
import com.amazonaws.services.s3.transfer._
import com.darktxns.Environment

@ThreadSafe
class S3Uploader(private val env: Environment)
{
    private val transferer = new TransferManager( env.awsCreds )

    def uploadDirectory(dir:File):MultipleFileUpload =
    {
        val listener = new ProgressListener
        {
            override def progressChanged(progressEvent: ProgressEvent):Unit =
            {
                println(s"Progress: ${dir.getName}, $progressEvent")
            }
        }

        val result = transferer.uploadDirectory(env.config.dataBucket, dir.getName, dir, true)
        result.addProgressListener(listener)

        println(s"Upload started for ${dir.getAbsolutePath}")
        result
    }
}
