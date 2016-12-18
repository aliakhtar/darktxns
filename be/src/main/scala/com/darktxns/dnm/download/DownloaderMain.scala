package com.darktxns.dnm.download

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

        future.onComplete(result =>
        {
            downloaded.incrementAndGet()
            unzip(result.get )
        })
    }

    private def unzip(download:DownloadResult) =
    {
        implicit val future = Future
        {
            blocking( new Unzipper().apply(download) )
        }

        future.onComplete(_ =>
            {
                println(s"Finished unzipping ${download.file.getAbsolutePath}")
                download.file.delete()
                unzipped.incrementAndGet
            } )
    }
}
