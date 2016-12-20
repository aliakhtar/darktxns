package com.darktxns.dnm.download

import java.io.File
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.darktxns.dnm.dataset.Dataset
import com.darktxns.{Environment, Task}
import org.apache.commons.io.FileUtils.byteCountToDisplaySize

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class S3UploaderMain(env:Environment, datasets: Traversable[Dataset]) extends Task
{
    val total = new AtomicInteger()
    val done = new AtomicInteger(0)
    val failed = new AtomicInteger(0)
    val s3Uploader = new S3Uploader(env)
    val bytesUploaded = new AtomicLong(0)

    private val dirSizeMap = mutable.LinkedHashMap.empty[String, String]

    override def begin(): Unit =
    {
        total.set( datasets.size )
        datasets.flatMap(_.directories).foreach(upload)
    }

    private def upload(directory: File): Unit =
    {
        Future( blocking( uploadBlocking(directory) ) )
    }


    private def uploadBlocking(directory: File):Unit =
    {
        try
        {
            val dirBytes = s3Uploader.uploadDirectory(directory)
            println(s"SUCCESSFULLY UPLOADED ${directory.getAbsolutePath}, ${byteCountToDisplaySize(dirBytes)}")
            println(status())
            bytesUploaded.getAndAdd( dirBytes )

            dirSizeMap += (directory.getName -> byteCountToDisplaySize(dirBytes))
            dirSizeMap.foreach(t => println(t.toString()))
        }
        catch
        {
            case e:Exception =>
            {
                failed.incrementAndGet()
                println(s"FAILED UPLOAD ${directory.getAbsolutePath}")
                e.printStackTrace()
            }
        }
    }

    override def finished(): Boolean =
    {
        done.get() >= total.get()
    }

    override def status(): String =
    {
        s"${done.get()} / ${total.get()} uploaded, ${failed.get()} failed, " +
            s" ${byteCountToDisplaySize(bytesUploaded.get())} uploaded."
    }
}
