package com.darktxns.dnm.download

import java.io.File
import java.nio.file.{Files, Paths}

import com.amazonaws.annotation.ThreadSafe
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.model.{ObjectMetadata, PutObjectRequest}
import com.amazonaws.services.s3.transfer._
import com.amazonaws.services.s3.{AmazonS3Client, S3ClientOptions}
import com.darktxns.Environment
import org.apache.commons.io.FileUtils

import scala.collection.mutable

@ThreadSafe
class S3Uploader(private val env: Environment) extends ObjectMetadataProvider
{
    private val s3 = new AmazonS3Client(env.awsCreds)
    s3.setRegion(Region.getRegion(Regions.US_WEST_2))
    s3.setS3ClientOptions(S3ClientOptions.builder().enableDualstack().setAccelerateModeEnabled(true).build())

    private val transferer = new TransferManager( s3 )

    def uploadDirectory(dir:File):Long =
    {
        var uploaded = 0L

        var files = mutable.ListBuffer.empty[File]
        val dirs = mutable.ListBuffer.empty[File]

        dir.listFiles().foreach(f =>
        {
            if (f.isDirectory)
                dirs += f
            else
                files += f
        })

        println(s"Starting upload for ${dir.getAbsolutePath}, files: ${files.length} , dirs: ${dirs.length}")

        uploaded += files.map(uploadFile).sum
        uploaded += dirs.map(uploadDirectory).sum

        uploaded
    }

    def uploadFile(file:File):Long =
    {
        val key = file.getAbsolutePath.replace("/data/raw", "")
        val req = new PutObjectRequest(env.config.dataBucket, key, file)

        provideObjectMetadata(file, req.getMetadata)

        val upload = transferer.upload(req)

        val fileSize = upload.getProgress.getTotalBytesToTransfer
        val fileSizeFriendly = FileUtils.byteCountToDisplaySize(fileSize)
        println(s"Upload started for ${file.getAbsolutePath} -> $key, $fileSizeFriendly to go")


        upload.waitForCompletion()
        println(s"Upload done for ${file.getAbsolutePath} -> $key")
        fileSize
    }


    override def provideObjectMetadata(file: File, metadata: ObjectMetadata): Unit =
    {
         metadata.setContentType( Files.probeContentType( Paths.get(file.getAbsolutePath) ) )
    }
}
