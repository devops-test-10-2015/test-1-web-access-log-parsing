import org.kevinhcross.tools.ParseApacheLogJson
import spock.lang.Specification

class ParseApacheLogJsonTest extends Specification{
  def expectedResults = [
    """{"minute":"2015-03-30 05:04 +0100","successCount":2,"errorCount":0,"meanResponseTime":150000,"megaBytes":150}""",
    """{"minute":"2015-03-30 05:05 +0100","successCount":2,"errorCount":0,"meanResponseTime":350000,"megaBytes":350}""",
    """{"minute":"2015-03-30 05:06 +0100","successCount":2,"errorCount":0,"meanResponseTime":550000,"megaBytes":550}"""
  ]

  def "verify minute reports"() {
    setup:
    ParseApacheLogJson parseApacheLogJson = new ParseApacheLogJson("test_data/6_200s_over_3_minutes.json")
    when:
    parseApacheLogJson.parseLogs()
    then:
    expectedResults.eachWithIndex { String expected, int i ->
      parseApacheLogJson.minuteStatsList[i].toString() == expected
    }
  }

  def "verify total report"() {
    setup:
    ParseApacheLogJson parseApacheLogJson = new ParseApacheLogJson("test_data/6_200s_over_3_minutes.json")
    when:
    parseApacheLogJson.parseLogs()
    then:
    parseApacheLogJson
  }


}
