package code.model

import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.ResponseList
import twitter4j.QueryResult
import twitter4j.Query
import twitter4j.Paging
import net.liftweb.util.Props

class TwitterAPI {
	val twitter = (new TwitterFactory()).getInstance()
	
	twitter.setOAuthConsumer(Props.get("twitter.oauth.consumerKey").openOr(""),
      Props.get("twitter.oauth.consumerSecret").openOr(""))
      
    twitter.setOAuthAccessToken(new AccessToken(Props.get("twitter.oauth.accessToken").openOr(""),
      Props.get("twitter.oauth.accessTokenSecret").openOr("")))

    // Posting a Tweet
    def setStatus(status: String) = this.twitter.updateStatus(status)
}