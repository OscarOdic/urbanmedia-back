package controllers

import javax.inject._

import models.Reaction
import models.Tables.{comments, warnings}
import play.api.mvc.{Action, Controller}
import services.AuthService
import slick.jdbc.PostgresProfile.api._
import utils.SlickDatabase

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ReactionController @Inject() extends Controller {
  def addComment = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = DBIO.seq(
        comments += Reaction(
          placeId = Some(request.body("placeid").head.toInt),
          user = account.userName,
          message = request.body("message").head
        )
      )
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def addWarning = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = DBIO.seq(
        warnings += Reaction(
          placeId = Some(request.body("placeid").head.toInt),
          user = account.userName,
          message = request.body("message").head
        )
      )
      db.run(insert).map(_ => Ok("added"))
    }
  }
}
