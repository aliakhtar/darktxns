package com.darktxns.dnm.download

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import com.darktxns.dnm.dataset.Dataset
import com.darktxns.{Environment, Task}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class S3UploaderMain(env:Environment, datasets: Traversable[Dataset]) extends Task
{
    val total = new AtomicInteger()
    val done = new AtomicInteger(0)
    val failed = new AtomicInteger(0)
    val s3Uploader = new S3Uploader(env)

    override def begin(): Unit =
    {
        datasets.flatMap(_.directories).foreach(upload)
    }


    private def upload(directory: File):Unit =
    {
        implicit val future = Future{ blocking( s3Uploader.uploadDirectory(directory) ) }
        future.onComplete(result =>
        {
            if (! result.isSuccess)
            {
                println(s"FAILED UPLOAD ${directory.getAbsolutePath}")
                result.failed.get.printStackTrace()
                failed.incrementAndGet()
            }
            else
                println(s"SUCCESSFULLY UPLOADED ${directory.getAbsolutePath}")

            done.incrementAndGet()
        })

        total.incrementAndGet()
    }

    override def finished(): Boolean =
    {
        done.get() >= total.get()
    }

    override def status(): String =
    {
        s"${done.get()} / ${total.get()} uploaded, ${failed.get()} failed."
    }
}
