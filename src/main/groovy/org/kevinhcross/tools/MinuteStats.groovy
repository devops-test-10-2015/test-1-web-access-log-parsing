package org.kevinhcross.tools

import groovy.json.JsonBuilder

class MinuteStats {
  Integer successCount = 0
  Integer errorCount = 0
  String currentTimeStamp
  List<Integer> requestTimes = []
  List<Integer> bytesServedList = []

  // Ref : http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
  void addToRequestCounts(String httpCode){
    if (httpCode.startsWith("2")){
      successCount += 1
    } else if (httpCode.startsWith("4") || httpCode.startsWith("5")){
      errorCount += 1
    }
  }

  void addToRequestTimes(String requestTimeStr) {
    if(requestTimeStr == null || requestTimeStr == 'null') {
      println "WARNING: null value found"
      requestTimes << 0
    } else {
      requestTimes << requestTimeStr.toInteger()
    }
  }

  void addToBytes(String mb) {
    if(mb == null || mb == 'null') {
      println "WARNING: null value found"
      bytesServedList << 0
    } else {
      bytesServedList << mb.toInteger()
    }
  }

  BigDecimal getMeanResponseTime() {
    if (requestTimes.size() == 0) {
      return  0
    }
    Integer total = 0
    requestTimes.each { respTime ->
      total += respTime
    }
    return total/requestTimes.size()
  }

  BigDecimal getMegaBytes() {
    BigDecimal bytesTotal = new BigDecimal(0)
    bytesServedList.each { bytes ->
      bytesTotal += bytes
    }
    return bytesTotal/bytesServedList.size()
  }

  String toString() {
    JsonBuilder builder = new JsonBuilder()
    builder {
      minute currentTimeStamp
      successCount successCount
      errorCount errorCount
      meanResponseTime meanResponseTime
      megaBytes megaBytes
    }

    return builder.toString()
  }
}
