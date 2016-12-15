package se.hedefalk

import net.liftweb.common.Box
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.http._
import argonaut.Json
import net.liftweb.http.InMemoryResponse
import net.liftweb.http.rest.RestHelper
import net.liftweb.util.Props


package argonautlift {
  case class ArgonautResponse(json: Json, headers: List[(String, String)], cookies: List[HTTPCookie], code: Int) extends LiftResponse {
    def toResponse = {
      val bytes = (if (Props.devMode) {
        json.spaces2
      } else {
        json.nospaces
      }).getBytes("UTF-8")

      InMemoryResponse(bytes, ("Content-Length", bytes.length.toString) :: ("Content-Type", "application/json; charset=utf-8") :: headers, cookies, code)
    }
  }

  object ArgonautResponse {
    def headers: List[(String, String)] = S.getResponseHeaders(Nil)

    def cookies: List[HTTPCookie] = S.responseCookies

    def apply(json: Json): LiftResponse =
      new ArgonautResponse(json, headers, cookies, 200)

  }

  trait ArgonautRestHelper {
    self: RestHelper =>

    type ArgonautResponse = argonautlift.ArgonautResponse

    protected lazy val ArgonautPost = new TestPost[argonaut.Json] {
      override def testResponse_?(r: Req): Boolean = true

      override def body(req: Req): Box[argonaut.Json] = {

        def r = """; *charset=(.*)""".r
        def r2 = """[^=]*$""".r
        def charSet: String = req.contentType.flatMap(ct => r.findFirstIn(ct).flatMap(r2.findFirstIn)).getOrElse("UTF-8")

        req.body.flatMap {
          bytes => argonaut.Parse.parseOption(new String(bytes, charSet))
        }
      }
    }
  }

}
