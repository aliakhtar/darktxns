package com.darktxns

import com.darktxns.io.Reader
import org.jsoup.Jsoup

object Main
{
    def main(args: Array[String]): Unit =
    {
        val rawHtml = Reader readResource "dnmarchives.html"
        val tree = Jsoup parse rawHtml
        println "tree: " + tree.head
    }
}
