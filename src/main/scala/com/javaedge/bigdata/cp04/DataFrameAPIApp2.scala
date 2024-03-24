package com.javaedge.bigdata.cp04

import org.apache.spark.sql.{DataFrame, SparkSession}

object DataFrameAPIApp2 {

  def main(args: Array[String]): Unit = {
    val projectRootPath = "/Users/javaedge/Downloads/soft/sparksql-train"
    val spark = SparkSession.builder()
      .master("local").appName("DataFrameAPIApp")
      .getOrCreate()

    val zips: DataFrame = spark.read.json(projectRootPath + "/data/zips.json")
    // 查看schema信息
    zips.printSchema()

    /**
     * loc的信息没用展示全，超过一定长度就使用...来展示
     * 默认只显示前20条：
     * show() ==> show(20) ==> show(numRows, truncate = true)
     */
    zips.show(5)
    zips.show(5, truncate = false)

    zips.head(3).foreach(println)
    zips.first()
    zips.take(5)

    val count: Long = zips.count()
    println(s"Total Counts: $count")

    // 过滤出大于40000，字段重新命名
    zips.filter(zips.col("pop") > 40000)
      .withColumnRenamed("_id", "new_id")
      .show(5, truncate = false)


    import org.apache.spark.sql.functions._
    // 统计加州pop最多的10个城市名称和ID  desc是一个内置函数
    zips.select("_id", "city", "pop", "state")
      .filter(zips.col("state") === "CA")
      .orderBy(desc("pop"))
      .show(5, truncate = false)

    zips.createOrReplaceTempView("zips")
    spark.sql("select _id,city,pop,state" +
      "from zips where state='CA'" +
      "order by pop desc" +
      "limit 10").show()

    spark.stop()
  }
}
