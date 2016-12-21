package com.darktxns.dnm.download

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.concurrent.atomic.AtomicLong

import com.amazonaws.annotation.ThreadSafe
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.model.{ObjectMetadata, PutObjectRequest}
import com.amazonaws.services.s3.transfer._
import com.amazonaws.services.s3.{AmazonS3Client, S3ClientOptions}
import com.darktxns.Environment
import com.darktxns.dnm.dataset.Dataset
import org.apache.commons.io.FileUtils.byteCountToDisplaySize

import scala.collection.mutable

@ThreadSafe
class S3Uploader(private val env: Environment)
{
    private val s3 = new AmazonS3Client(env.awsCreds)
    s3.setRegion(Region.getRegion(Regions.US_WEST_2))
    s3.setS3ClientOptions(S3ClientOptions.builder().enableDualstack().setAccelerateModeEnabled(true).build())

    private val transferer = new TransferManager( s3 )

    val totalUploaded = new AtomicLong(0)

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

        println(s"Uploaded directory: ${dir.getAbsoluteFile}: ${byteCountToDisplaySize(uploaded)}, " +
            s"total: ${byteCountToDisplaySize(totalUploaded.get())}")
        uploaded
    }

    def uploadFile(file:File):Long =
    {
        val key = getKey(file)
        val req = new PutObjectRequest(env.config.dataBucket, key, file)
        req.setMetadata( metadata(file) )

        try
        {
            val upload = transferer.upload(req)

            val fileSize = upload.getProgress.getTotalBytesToTransfer

            upload.waitForCompletion()
            totalUploaded.addAndGet( fileSize )
            fileSize
        }
        catch
        {
            case e:Exception =>  e.printStackTrace(); 0
        }
    }

    private def metadata(file: File):ObjectMetadata =
    {
        val metadata = new ObjectMetadata()
        metadata.setContentType( Files.probeContentType( Paths.get(file.getAbsolutePath) ) )

        metadata
    }

    private def getKey(file: File): String =
    {
        var key = file.getAbsolutePath.replace(Dataset.UNZIP_DIR, "")
        val parts = mutable.ArrayBuffer.empty[String]

        key.split('/').foreach(s => parts += s)

        if (parts(0) == parts(1))
            parts.remove( 0 )

        key = parts.mkString("/").replace(" ","-")
        if (key.length > 63)
            key = key.substring(0, 63)


    }
}
