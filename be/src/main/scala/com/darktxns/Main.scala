package com.darktxns

object Main
{
    def main(args: Array[String]): Unit =
    {
        val env = Environment.build()

        println(env.toString)

        /*val task:Task = new DownloaderMain
        task.begin()

        while (! task.finished())
        {
            println( task.status() )
            Thread.sleep(10000)
        }*/
    }
}
