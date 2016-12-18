package com.darktxns.dnm.download

import java.io.File
import java.util.concurrent.atomic.AtomicInteger

import com.darktxns.Task
import com.darktxns.io.Reader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class DownloaderMain extends Task
{
    private val rawHtml = Reader readResource "dnmarchives.html"
    private val links = new DownloadLinkExtractor(rawHtml).call()

    private val toDownload = 1
    private val toUnzip = 1

    private val downloaded = new AtomicInteger(0)
    private val unzipped = new AtomicInteger(0)


    override def begin():Unit =
    {
        println("Starting download..")

        val toDl = links.find(_.fileName == "zanzibarspice.tar.xz").get

        download(toDl)
    }

    override def finished():Boolean = downloaded.get() >= toDownload && unzipped.get() >= toUnzip

    private def download(link: DownloadLink): Unit =
    {
        val downloader = new Downloader(link)
        implicit val future = Future
        {
            blocking
            {
                downloader.get()
            }
        }

        future.onComplete(file =>
        {
            downloaded.incrementAndGet()
            unzip(file.get )
        })
    }

    private def unzip(gzippedFile:File) =
    {
        implicit val future = Future
        {
            blocking( new Unzipper().apply(gzippedFile) )
        }

        future.onComplete(_ =>
            {
                println(s"Finished unzipping ${gzippedFile.getAbsolutePath}")
                gzippedFile.delete()
                unzipped.incrementAndGet
            } )
    }
}
