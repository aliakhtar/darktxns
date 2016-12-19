package com.darktxns.dnm.download

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.concurrent.atomic.AtomicLong

import com.amazonaws.annotation.ThreadSafe
import com.amazonaws.event.ProgressEventType.TRANSFER_COMPLETED_EVENT
import com.amazonaws.event.{ProgressEvent, ProgressListener}
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.transfer._
import com.darktxns.Environment

@ThreadSafe
class S3Uploader(private val env: Environment) extends ObjectMetadataProvider
{
    private val transferer = new TransferManager( env.awsCreds )

    def uploadDirectory(dir:File):Long =
    {
        val upload = transferer.uploadDirectory(env.config.dataBucket, dir.getName, dir, true, this)

        val bytesUploaded = new AtomicLong(0L)
        println(s"Upload started for ${dir.getAbsolutePath}")

        // Block until the upload finishes
        upload.getSubTransfers.forEach(u =>
        {
            u.addProgressListener(new ProgressListener
            {
                override def progressChanged(e: ProgressEvent):Unit =
                {
                    if (e.getEventType != TRANSFER_COMPLETED_EVENT)
                        return

                    println(s"${u.getDescription}")
                    println( s"transfered: ${e.getBytesTransferred}, bytes: ${e.getBytes}" )

                    val bytes = if (e.getBytesTransferred > 0) e.getBytesTransferred else e.getBytes
                    bytesUploaded.getAndAdd( bytes )
                }
            })
        })
        upload.waitForCompletion()
        bytesUploaded.get()
    }


    override def provideObjectMetadata(file: File, metadata: ObjectMetadata): Unit =
    {
         metadata.setContentType( Files.probeContentType( Paths.get(file.getAbsolutePath) ) )
    }
}
