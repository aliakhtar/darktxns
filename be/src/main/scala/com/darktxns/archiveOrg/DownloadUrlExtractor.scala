package com.darktxns.archiveOrg

import java.util.concurrent.Callable

import org.jsoup.Jsoup

import scala.collection.mutable.ListBuffer

class DownloadUrlExtractor(rawHtml: String) extends Callable[Traversable[String]]
{
    override def call(): Traversable[String] =
    {
        val allLinks = Jsoup.parse(rawHtml).body().select("a")

        val result = ListBuffer.empty[String]

        allLinks.forEach(a =>
        {
            val href = a.attr("href")
            if (href != null && href.endsWith(".tar.xz"))
                result += href
        })

        result
    }
}
