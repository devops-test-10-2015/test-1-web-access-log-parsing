import org.kevinhcross.tools.ParseApacheLogJson
import spock.lang.Specification

class ParseApacheLogJsonTest extends Specification{
  def expectedResults = [
    """{"minute":"2015-03-30 05:04 +0100","successCount":2,"errorCount":0,"meanResponseTime":150000,"meanMegaBytes":150}""",
    """{"minute":"2015-03-30 05:05 +0100","successCount":2,"errorCount":0,"meanResponseTime":350000,"meanMegaBytes":350}""",
    """{"minute":"2015-03-30 05:06 +0100","successCount":2,"errorCount":0,"meanResponseTime":550000,"meanMegaBytes":550}"""
  ]

  def optionsExpected = """{"minute":"2015-03-30 05:05 +0100","successCount":10,"errorCount":0,"meanResponseTime":100,"meanMegaBytes":1000}"""

  def mixedTimesTest = [
    """{"minute":"2015-03-30 05:04 +0100","successCount":2,"errorCount":0,"meanResponseTime":1000,"meanMegaBytes":1000}""",
    """{"minute":"2015-03-30 05:05 +0100","successCount":4,"errorCount":0,"meanResponseTime":1000,"meanMegaBytes":1000}""",
    """{"minute":"2015-03-30 05:06 +0100","successCount":2,"errorCount":0,"meanResponseTime":1000,"meanMegaBytes":1000}"""
  ]

  def "verify minute reports"() {
    setup:
    ParseApacheLogJson parseApacheLogJson = new ParseApacheLogJson("test_data/6_200s_over_3_minutes.json")
    when:
    parseApacheLogJson.parseLogs()
    then:
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:04 +0100").toString() == expectedResults[0]
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:05 +0100").toString() == expectedResults[1]
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:06 +0100").toString() == expectedResults[2]
  }


  def "verify options reports"() {
    setup:
    ParseApacheLogJson parseApacheLogJson = new ParseApacheLogJson("test_data/with_options.json")
    when:
    parseApacheLogJson.parseLogs()
    then:
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:05 +0100").toString() == optionsExpected
  }

  def "mixed times"() {
    setup:
    ParseApacheLogJson parseApacheLogJson = new ParseApacheLogJson("test_data/mixed_times.json")
    when:
    parseApacheLogJson.parseLogs()
    then:
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:04 +0100").toString() == mixedTimesTest[0]
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:05 +0100").toString() == mixedTimesTest[1]
    parseApacheLogJson.minuteStatsMap.get("2015-03-30 05:06 +0100").toString() == mixedTimesTest[2]
  }



}
