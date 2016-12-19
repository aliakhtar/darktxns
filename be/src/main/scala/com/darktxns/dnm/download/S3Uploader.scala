package com.darktxns.dnm.download

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.concurrent.atomic.AtomicLong

import com.amazonaws.annotation.ThreadSafe
import com.amazonaws.event.ProgressEventType.{CLIENT_REQUEST_FAILED_EVENT, TRANSFER_FAILED_EVENT, TRANSFER_PART_FAILED_EVENT}
import com.amazonaws.event.{ProgressEvent, ProgressEventType, ProgressListener}
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.transfer._
import com.amazonaws.services.s3.{AmazonS3Client, S3ClientOptions}
import com.darktxns.Environment
import org.apache.commons.io.FileUtils

@ThreadSafe
class S3Uploader(private val env: Environment) extends ObjectMetadataProvider
{
    private val s3 = new AmazonS3Client(env.awsCreds)
    s3.setRegion(Region.getRegion(Regions.US_WEST_2))
    s3.setS3ClientOptions(S3ClientOptions.builder().enableDualstack().setAccelerateModeEnabled(true).build())

    private val transferer = new TransferManager( s3 )

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
                    if (e.getEventType != ProgressEventType.TRANSFER_COMPLETED_EVENT)
                    {
                        if (e.getEventType == CLIENT_REQUEST_FAILED_EVENT || e.getEventType == TRANSFER_FAILED_EVENT
                            || e.getEventType == TRANSFER_PART_FAILED_EVENT)
                        {
                            println( s"${u.getDescription}, ${e.getEventType}" )
                        }

                        return
                    }

                    bytesUploaded.getAndAdd( u.getProgress.getBytesTransferred )
                    val sentUnderThisTransfer = FileUtils.byteCountToDisplaySize( bytesUploaded.get() )

                    println(s"${dir.getName}, $sentUnderThisTransfer")
                }
            })
        })
        upload.waitForCompletion()
        println(s"Upload finished for ${dir.getAbsolutePath}")
        bytesUploaded.get()
    }


    override def provideObjectMetadata(file: File, metadata: ObjectMetadata): Unit =
    {
         metadata.setContentType( Files.probeContentType( Paths.get(file.getAbsolutePath) ) )
    }
}
