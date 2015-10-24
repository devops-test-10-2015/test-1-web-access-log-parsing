package org.kevinhcross.tools

import groovy.json.JsonSlurper
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

class ParseApacheLogJson {
  static final DateTimeFormatter fmt = ISODateTimeFormat.dateTime()

  File jsonDataFile
  JsonSlurper slurper = new JsonSlurper()
  Map<String, MinuteStats> minuteStatsMap = [:]

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
      processMinuteData(result)
    }
  }

  void processMinuteData(def le) {
    String minuteStr = fmt.parseDateTime(le."@timestamp").toString("YYYY-MM-dd HH:mm Z")
    MinuteStats minuteStats = minuteStatsMap.get(minuteStr)
    if (minuteStats == null) {
      minuteStats = new MinuteStats(currentTimeStamp: minuteStr)
      minuteStatsMap.put(minuteStr, minuteStats)
    }
    minuteStats.addToRequestCounts(le.response)
    minuteStats.addToRequestTimes(le.time_to_serve)
    minuteStats.addToBytes(le.bytes)
  }

  void printReport() {
    minuteStatsMap.each { minuteStr, minuteStats ->
      println minuteStats
    }
  }
}
