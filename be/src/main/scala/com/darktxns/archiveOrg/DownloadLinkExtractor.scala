package com.darktxns.archiveOrg

import java.util.concurrent.Callable

import org.jsoup.Jsoup

import scala.collection.mutable.ListBuffer

class DownloadLinkExtractor(rawHtml: String) extends Callable[Traversable[DownloadLink]]
{
    override def call(): Traversable[DownloadLink] =
    {
        val allLinks = Jsoup.parse(rawHtml).body().select("a")

        val result = ListBuffer.empty[DownloadLink]

        allLinks.forEach(a =>
        {
            val href = a.attr("href")
            if (href != null && href.endsWith( DownloadLink.EXTENSION ))
                result += new DownloadLink( href )
        })

        result
    }
}
