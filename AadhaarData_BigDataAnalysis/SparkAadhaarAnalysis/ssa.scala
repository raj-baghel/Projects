package com.stdatalabs.SparkAadhaarAnalysis
// Scala Imports
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql._
import org.apache.spark.sql.SQLContext._
import org.apache.spark.sql.hive.HiveContext

object UIDStats {

  val conf = new SparkConf().setAppName("Aadhaar dataset analysis using Spark")
  val sc = new SparkContext(conf)

  val hiveContext = new HiveContext(sc)

  import hiveContext.implicits._

  def main(args: Array[String]) {
    
    // Register dataset as a temp table
    val uidEnrolmentDF = hiveContext.read.format("com.databricks.spark.csv").option("header", "true").option("inferSchema", "true").load(args(0))
    uidEnrolmentDF.registerTempTable("uid_enrolments_detail")

    // Create a hive table with Total Aadhaar's generated for each state
    val stateWiseCountDF = hiveContext.sql("""
                                              | SELECT State, 
                                              |        SUM(`Aadhaar generated`) as count
                                              | FROM uid_enrolments_detail 
                                              | GROUP BY state 
                                              | ORDER BY count DESC""".stripMargin)
                                              
    stateWiseCountDF.write.mode("overwrite").saveAsTable("uid.state_wise_count")

    // Create a hive table with Total Aadhaar's generated by each enrolment agency
    val maxEnrolmentAgencyDF = hiveContext.sql("""
                                                  | SELECT `Enrolment Agency` as Enrolment_Agency, 
                                                  |        SUM(`Aadhaar generated`) as count 
                                                  | FROM uid_enrolments_detail 
                                                  | GROUP BY `Enrolment Agency` 
                                                  | ORDER BY count DESC""".stripMargin)
                                                  
    maxEnrolmentAgencyDF.write.mode("overwrite").saveAsTable("uid.agency_wise_count")

    // Create hive table with top 10 districts with maximum Aadhaar's generated for both Male and Female
    val districtWiseGenderCountDF = hiveContext.sql("""
                                                       | SELECT District, 
                                                       |        count(CASE WHEN Gender='M' THEN 1 END) as male_count, 
                                                       |        count(CASE WHEN Gender='F' THEN 1 END) as FEMALE_count 
                                                       | FROM uid_enrolments_detail 
                                                       | GROUP BY District
                                                       | ORDER BY male_count DESC, FEMALE_count DESC
                                                       | LIMIT 10""".stripMargin)
                                                       
    districtWiseGenderCountDF.write.mode("overwrite").saveAsTable("uid.district_wise_gndr_count")
  }

}
