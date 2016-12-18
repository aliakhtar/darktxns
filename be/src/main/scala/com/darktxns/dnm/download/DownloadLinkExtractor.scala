package com.darktxns.dnm.download

import java.util.concurrent.Callable

import com.darktxns.dnm.dataset.Dataset
import org.jsoup.Jsoup

import scala.collection.mutable.ListBuffer

class DownloadLinkExtractor(rawHtml: String)
    extends Callable[Traversable[Dataset]]
{
    override def call(): Traversable[Dataset] =
    {
        val allLinks = Jsoup.parse(rawHtml).body().select("a")

        val result = ListBuffer.empty[Dataset]

        allLinks.forEach(a =>
        {
            val href = a.attr("href")
            if (href != null && href.endsWith( Dataset.EXTENSION ))
                result += new Dataset( href ) //href will be just the file name atm, e.g file.xz
        })

        result
    }
}
