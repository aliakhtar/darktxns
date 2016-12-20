package com.darktxns.dnm.download

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.concurrent.atomic.AtomicLong

import com.amazonaws.annotation.ThreadSafe
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
        println(s"Starting upload for ${dir.getAbsolutePath}")
        val upload = transferer.uploadDirectory(env.config.dataBucket, dir.getName, dir, true, this)

        val bytesUploaded = new AtomicLong( upload.getProgress.getTotalBytesToTransfer )
        val toXfer = FileUtils.byteCountToDisplaySize(bytesUploaded.get())
        println(s"Upload started for ${dir.getAbsolutePath}, $toXfer to go")


        while (upload.getProgress.getPercentTransferred < 100)
        {
            val xfered = FileUtils.byteCountToDisplaySize( upload.getProgress.getBytesTransferred )
            println(s"${upload.getProgress.getPercentTransferred}% done, $xfered / $toXfer , ${dir.getName}")

            Thread.sleep(1000)
        }


        upload.waitForCompletion()
        println(s"Upload finished for ${dir.getAbsolutePath}")
        bytesUploaded.get()
    }


    override def provideObjectMetadata(file: File, metadata: ObjectMetadata): Unit =
    {
         metadata.setContentType( Files.probeContentType( Paths.get(file.getAbsolutePath) ) )
    }
}
