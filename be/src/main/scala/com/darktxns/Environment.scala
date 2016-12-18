package com.darktxns

import com.amazonaws.auth.{AWSCredentials, BasicAWSCredentials}
import com.darktxns.io.Reader
import org.json4s._
import org.json4s.native.JsonMethods._

object Environment
{
    def build():Environment =
    {
        val json = Reader.readResource("config.json")
        val config = parse(json).extract[Config]

        val creds = new BasicAWSCredentials(config.awsAccessKey, config.secretKey)
        new Environment(creds)
    }
}

class Environment(val awsCreds:AWSCredentials)
{

}
