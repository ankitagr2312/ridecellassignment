import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object MessageExtractor {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("Ridecell")
      .master("local[*]")
      .getOrCreate()

    import spark.sqlContext.implicits._

    val incomingDataSchema =
      StructType(
        StructField("timeValue", StringType, true) ::
          StructField("type", StringType, false) ::
          StructField("incoming_url", StringType, false) ::
          StructField("microservice", StringType, false) :: Nil)


    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "testTopic")
      .option("startingOffsets", "latest")
      .load()

    val encoder = RowEncoder(incomingDataSchema)

    df.printSchema()
    val processedData = df.selectExpr("CAST(value AS STRING)").as[String].map(r => {
      val dataSplit  = r.toString().split(",")
      Row(dataSplit(0).asInstanceOf[String].slice(0,16),dataSplit(0),dataSplit(0),dataSplit(0))
    })(encoder)


    val aggregatedData  = processedData.groupBy("timeValue").count()

    val outputDf = aggregatedData.selectExpr("CAST(timeValue as STRING) key", "CAST(Concat(timeValue,' ', count) as STRING) value")
      .writeStream
      .format("kafka")
      .outputMode("complete")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("checkpointLocation", "/home/mudita/code/ridecellassignment/checkpointDir")
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .option("topic", "outputTopic")
      .start()

    outputDf.awaitTermination()
  }
}

