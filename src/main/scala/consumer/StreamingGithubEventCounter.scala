package consumer

import kafka.serializer.StringDecoder
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

object StreamingGithubEventCounter {
  implicit val formats = DefaultFormats

  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: StreamingGithubEventCounter <brokers> <topics>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("StreamingGithubEventCounter")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    ssc.checkpoint(".")

    val kafkaParams = Map[String, String]("metadata.broker.list" -> args(0))
    val events = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, args(1).split(",").toSet)

    events
      .map(event => (parse(event._2) \ "type").extract[String])
      .map(eventType => (eventType, 1L))
      .reduceByKey(_ + _)
      .updateStateByKey(updateCounters)
      .print(25)

    ssc.start()
    ssc.awaitTermination()
  }

  def updateCounters(occurencesThisRun: Seq[Long], existingOccurences: Option[Long]): Option[Long] = {
    Some(occurencesThisRun.sum + existingOccurences.getOrElse(0L))
  }
}