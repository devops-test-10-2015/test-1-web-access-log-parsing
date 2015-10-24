package org.kevinhcross.tools

import groovy.json.JsonSlurper
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

class ParseApacheLogJson {
  static final DateTimeFormatter fmt = ISODateTimeFormat.dateTime()

  File jsonDataFile
  JsonSlurper slurper = new JsonSlurper()
  List<Object> logEntryList = []
  DateTime currentTimeStamp
  List<MinuteStats> minuteStatsList = []

  public static void main(args) {
    if (args.size() < 1) {
      println "Please supply a json data file"
      System.exit(1)
    }
    ParseApacheLogJson parseApacheLogJson = new ParseApacheLogJson(args[0])
    parseApacheLogJson.parseLogs()
    parseApacheLogJson.printReport()
  }

  ParseApacheLogJson(String jsonFileStr) {
    jsonDataFile = new File(jsonFileStr)
    if (! jsonDataFile.exists()) {
      println "File not found : ${jsonDataFile.getCanonicalPath()}"
    }
  }

  void parseLogs() {
    jsonDataFile.eachLine { line ->
      def result = slurper.parseText(line)
      DateTime dt = fmt.parseDateTime(result."@timestamp")

      if (currentTimeStamp == null){
        currentTimeStamp = dt
      }
      if (dt.getMinuteOfDay() != currentTimeStamp.getMinuteOfDay() || dt.getDayOfYear() != currentTimeStamp.getDayOfYear()) {
        processMinuteDataAndDump()
      }
      logEntryList << result
      currentTimeStamp = dt
    }
    // Dump the last batch of data
    processMinuteDataAndDump()
  }

  void processMinuteDataAndDump() {
    MinuteStats minuteStats = new MinuteStats(currentTimeStamp: currentTimeStamp.toString("YYYY-MM-dd HH:mm Z"))
    minuteStatsList << minuteStats
    logEntryList.each { le ->
      minuteStats.addToRequestCounts(le.response)
      minuteStats.addToRequestTimes(le.time_to_serve)
      minuteStats.addToBytes(le.bytes)
    }
    logEntryList = []
  }

  void printReport() {
    minuteStatsList.each { minuteStats ->
      println minuteStats
    }
  }
}
