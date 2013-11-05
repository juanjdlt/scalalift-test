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
import code.model.TwitterAPI

object Login extends Loggable {

  def callback() : JsCmd = {
    val twitter = new TwitterAPI
    logger.info("The button was pressed")
    JsCmds.Alert("You clicked it:")
    
    //twitter setStatus "jeje"
  }

  def button = "img [onclick]" #> SHtml.ajaxInvoke(callback)
}