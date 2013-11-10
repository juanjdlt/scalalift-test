package code.model

import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.auth.RequestToken
import twitter4j.ResponseList
import twitter4j.QueryResult
import twitter4j.Query
import twitter4j.Paging
import twitter4j.TwitterException
import net.liftweb.util.Props
import net.liftweb.http.Req
import net.liftweb.http.LiftResponse
import net.liftweb.http.S
import net.liftweb.http.SessionVar
import net.liftweb.http.LiftResponse
import net.liftweb.common._
import net.liftweb.http.LiftRules
import net.liftweb.http.GetRequest


object TwitterAPI  extends Loggable  {

	val twitter = (new TwitterFactory()).getInstance()

	def setOauth() = {
		this.twitter.setOAuthConsumer(Props.get("twitter.oauth.consumerKey").openOr(""),
				Props.get("twitter.oauth.consumerSecret").openOr(""))
	}

	def setStatus(status: String) = {
	  if(sessionTwitter.isDefined)
	    this.twitter.updateStatus(status)
	  else
		S.error("Unable to post a tweet.")
	}

	def doAuth(req: Req): Box[LiftResponse] = {
	  
		this.setOauth
		sessionTwitter.set(Full(this.twitter))
		try {
			val callbackURL = req.hostAndPath + "/twitter/callback"
			val requestToken = this.twitter.getOAuthRequestToken(callbackURL)
			sessionRequestToken.set(Full(requestToken))
			S.redirectTo(requestToken.getAuthenticationURL)
		} catch {
			case te: TwitterException => {
				if (401 == te.getStatusCode) { // Invalid credentials exception
					te.printStackTrace
					logger.info("Unable to get the access token.")
				} else {
					te.printStackTrace
				}
				S.error("Unable to login with twitter.")
				S.redirectTo("/")
			}
		}
	}
	
	def processAuthCallback(req: Req): Box[LiftResponse] = {
		(sessionTwitter.is, sessionRequestToken.is, req.param("oauth_verifier")) match {
			case (Full(twitter), Full(requestToken), Full(verifier)) => {
				try {
					twitter.getOAuthAccessToken(requestToken, verifier)
					sessionRequestToken.remove
					val tuser = twitter.verifyCredentials
					logger.info("Getting the twitter user as " + tuser)
					S.redirectTo("/post_tweet")
				} catch {
					case e: TwitterException => {
						throw new Exception(e)
					}
				}	
			}
			case _ => {
				S.error("Authentication error")
				logger.info("Authentication error")
				S.redirectTo("/")
			}
		}
	}
	
	def doLogout(req: Req): Box[LiftResponse] = { 
		sessionTwitter.remove
		sessionRequestToken.remove
		S.redirectTo("/")
	}
}

object sessionTwitter extends SessionVar[Box[Twitter]](Empty)
object sessionRequestToken extends SessionVar[Box[RequestToken]](Empty)