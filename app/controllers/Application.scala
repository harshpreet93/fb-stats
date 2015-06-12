package controllers
import scalaj.http.Http
import play.api._
import play.api.mvc._
import play.api.libs.json._

class Application extends Controller {
    val appID = "759239334198549"
    val appSecret = "5cd848b47683d77fff39c70ec8e2876e"
    val redirect_uri = "http://localhost:9000/after-login"
    val appToken = "ccabba112f74a79f4ae350a5b06d655a"

      def index = Action {
        // Ok(views.html.index("Your new application is ready."))
        // Ok(views.html.home())
        Redirect("https://www.facebook.com/dialog/oauth?client_id=759239334198549&redirect_uri=http://localhost:9000/after-login"
            +"&scopes=user_likes")
      }



      def getTokenFromCode(code: String) = {
   //        GET https://graph.facebook.com/v2.3/oauth/access_token?
   //  client_id={app-id}
   // &redirect_uri={redirect-uri}
   // &client_secret={app-secret}
   // &code={code-parameter}
        var answer = Http("https://graph.facebook.com/v2.3/oauth/access_token")
        .param("client_id", appID)
        .param("redirect_uri", redirect_uri)
        .param("client_secret", appSecret)
        .param("code", code).asString.body


        val json = Json.parse(answer)
        val maybeToken = (json \ "access_token").asOpt[String]
        var tokenFinal = ""
        maybeToken match {
            case Some(token) => tokenFinal = token
            case None => throw new Exception("No Token")
        }
        tokenFinal
      }


      def authenticateToken(token: String) = {
    //       GET graph.facebook.com/debug_token?
    //  input_token={token-to-inspect}
    //  &access_token={app-token-or-admin-token}
          var answer = Http("https://graph.facebook.com/debug_token")
                        .param("input_token", token)
                        .param("access_token", appID+"|"+appSecret)
                        .asString.body
        answer
      }

      def loginSuccess(code: String, token: String) = Action{
          println("RECEIVED CODE")
          println(code)
        //   println(token)
          println()
        //   println(code)
          var token = getTokenFromCode(code)
          println(authenticateToken(token))
          Ok(views.html.success())
      }


      def tokenSuccess = Action{
          Ok(views.html.tokenSuccess())
      }


}
