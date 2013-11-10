package code
package snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml.{text,ajaxSubmit}
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.SetHtml
import xml.Text
import code.model.sessionTwitter
import net.liftweb.common.Full
import twitter4j.Twitter
import code.model.TwitterAPI

class TweetForm {
  val user = sessionTwitter.is.get.verifyCredentials
  val username = user.getScreenName
  val image_url = user.getProfileImageURL.toString
  def twitterUsername = "#username *" #> username
  def twitterPorfilePic = "img [src]" #> image_url
}

object TweetFormProcess extends {

  def render = {

    var msg = ""

    def process() : JsCmd = {
    	TwitterAPI.setStatus(msg)
    	SetHtml("result", Text("Tweet posted!"))
    }

    "@tweet" #> text(msg, s => msg = s) &
    "type=submit" #> ajaxSubmit("Tweet", process)
  }
}