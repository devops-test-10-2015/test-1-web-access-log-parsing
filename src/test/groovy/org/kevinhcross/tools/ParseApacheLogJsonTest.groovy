import org.kevinhcross.tools.ParseApacheLogJson
import spock.lang.Specification

class ParseApacheLogJsonTest extends Specification{
  def "someLibraryMethod returns true"() {
    setup:
    ParseApacheLogJson lib = new ParseApacheLogJson()
    when:
    def result = lib.someLibraryMethod()
    then:
    result == true
  }
}
