package code
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import net.liftweb.http.js._
import net.liftweb.http.SHtml
import net.liftweb.http.S

object Login extends Loggable {

  def callback() : JsCmd = {
    logger.info("The button was pressed")
    S.redirectTo("twitter/authenticate")
  }

  def button = "img [onclick]" #> SHtml.ajaxInvoke(callback)
}