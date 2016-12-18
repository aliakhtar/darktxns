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
    private var unzipped = 0


    override def begin():Unit =
    {
        println("Starting download..")

        val toDl = links.find(_.fileName == "zanzibarspice.tar.xz").get

        download(toDl)
    }

    override def finished():Boolean = downloaded.get() >= toDownload

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
            //Runtime.getRuntime.exec(s"tar -xvf ${dest.getAbsolutePath}").waitFor()
        })

        println("Callback set")
    }
}
