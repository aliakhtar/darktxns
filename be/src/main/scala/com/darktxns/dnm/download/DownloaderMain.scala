package com.darktxns.dnm.download

import com.darktxns.io.Reader

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class DownloaderMain
{
    def begin():Unit =
    {
        val rawHtml = Reader readResource "dnmarchives.html"
        val links = new DownloadLinkExtractor(rawHtml).call()

        println("Starting download..")

        val toDl = links.find(_.fileName == "zanzibarspice.tar.xz").get

        download(toDl)
    }

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
            println("File downloaded: " + file.get.getAbsolutePath)
            //Runtime.getRuntime.exec(s"tar -xvf ${dest.getAbsolutePath}").waitFor()
        })

        println("Callback set")
    }
}
