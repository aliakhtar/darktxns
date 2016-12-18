package com.darktxns.dnm.download

import java.io.File
import java.util.concurrent.atomic.{AtomicBoolean, AtomicLong}

import com.amazonaws.annotation.ThreadSafe
import com.amazonaws.event.ProgressEventType.TRANSFER_COMPLETED_EVENT
import com.amazonaws.event.{ProgressEvent, ProgressListener}
import com.amazonaws.services.s3.transfer._
import com.darktxns.Environment

@ThreadSafe
class S3Uploader(private val env: Environment)
{
    private val transferer = new TransferManager( env.awsCreds )

    def uploadDirectory(dir:File):Long =
    {
        val uploaded = new AtomicBoolean(false)
        val bytesUploaded = new AtomicLong(0L)
        val upload = transferer.uploadDirectory(env.config.dataBucket, dir.getName, dir, true)
        println(s"Upload started for ${dir.getAbsolutePath}")

        // Block until the upload finishes
        upload.addProgressListener(new ProgressListener
        {
            override def progressChanged(e: ProgressEvent):Unit =
            {
                if (e.getEventType != TRANSFER_COMPLETED_EVENT)
                    return

                println(s"Finished upload of ${dir.getAbsolutePath}")
                bytesUploaded.set( e.getBytesTransferred )
                uploaded.set(true)
            }
        })

        while (! uploaded.get())
        {
            Thread.sleep(1000)
        }

        bytesUploaded.get()
    }
}
