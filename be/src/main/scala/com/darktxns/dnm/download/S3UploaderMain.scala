package com.darktxns.dnm.download

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import com.darktxns.Task
import com.darktxns.dnm.dataset.Dataset

class S3UploaderMain(datasets: Traversable[Dataset]) extends Task
{
    val total = new AtomicInteger()
    val uploading = new AtomicInteger(0)
    val done = new AtomicInteger(0)


    override def begin(): Unit =
    {
        datasets.flatMap(_.directories).foreach(upload)
    }


    private def upload(directory: File):Unit =
    {
        println(s"Uploading ${directory.getAbsolutePath}")
        total.incrementAndGet()
    }

    override def finished(): Boolean =
    {
        done.get() >= total.get()
    }

    override def status(): String =
    {
        s"${done.get()} / ${total.get()} uploaded, ${done.get()} uploading."
    }
}
