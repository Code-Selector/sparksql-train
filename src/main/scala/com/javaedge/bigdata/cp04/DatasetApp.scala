package com.javaedge.bigdata.cp04

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object DatasetApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local").appName("DatasetApp")
      .getOrCreate()
    import spark.implicits._

    //    val ds: Dataset[Person] = Seq(Person("PK", "30")).toDS()
    //    ds.show()

    //    val primitiveDS: Dataset[Int] = Seq(1, 2, 3).toDS()
    //    primitiveDS.map(x => x+1).collect().foreach(println)

    val peopleDF: DataFrame = spark.read.json(
      "/Users/javaedge/Downloads/sparksql-train/data/people.json")
    val peopleDS: Dataset[Person] = peopleDF.as[Person]
    peopleDS.show(false)
    // 弱语言类型，运行时才会报错
    peopleDF.select("name").show()
    // 编译期报错
    peopleDS.map(x => x.name).show()

    spark.stop()
  }

  private case class Person(name: String, age: String)

}
