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

    private val toDownload = links.size
    private val toUnzip = 0

    private val downloaded = new AtomicInteger(0)
    private val unzipped = new AtomicInteger(0)


    override def begin():Unit =
    {
        println("Starting downloads..")

        links.foreach(download)

        println("All download futures added")
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
            if (result.isSuccess)
                downloaded.incrementAndGet()
            else
                println(s"FAILED DOWNLOADING ${link.fullUrl}. ${future.toString}")
            //unzip(result.get )
        })

    }

    /**
      * Umm yeah so the following odes not work, kept here in case its needed for something later. I know i could dig
      * it up thru the git history but this is a hackathon and i want to waste less time.
      */
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

    override def status(): String = s"Downloaded: ${downloaded.get()} / $toDownload , Unzipped: ${unzipped.get()} / $toUnzip"
}
