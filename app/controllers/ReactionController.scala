package controllers

import javax.inject._

import models.Reaction
import models.Tables.{comments, warnings}
import play.api.mvc.{Action, Controller}
import services.AuthService
import slick.jdbc.PostgresProfile.api._
import utils.SlickDatabase

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ReactionController @Inject() extends Controller {
  def addComment(placeid: Int) = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = DBIO.seq(
        comments += Reaction(
          placeId = Some(placeid),
          user = account.userName,
          message = request.body("message").head
        )
      )
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def editComment(commentid: Int) = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val query = comments.filter(_.id === commentid)
      db.run(query.result).flatMap(_.head match {
        case comment if comment.user == account.userName =>
          db.run(query.update(comment.copy(message = request.body("message").head))).map(_ => Ok("edited"))
        case _ => Future(Unauthorized(s"${account.userName} is unauthorized to edit this comment"))
      })
    }
  }

  def deleteComment(commentid: Int) = Action.async { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val query = comments.filter(_.id === commentid)
      db.run(query.map(_.user).result).flatMap(_.head match {
        case user if user == account.userName => db.run(query.delete).map(_ => Ok("deleted"))
        case _ => Future(Unauthorized(s"${account.userName} is unauthorized to delete this comment"))
      })
    }
  }

  def addWarning(placeid: Int) = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = DBIO.seq(
        warnings += Reaction(
          placeId = Some(placeid),
          user = account.userName,
          message = request.body("message").head
        )
      )
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def deleteWarning(warningid: Int) = Action.async { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val query = warnings.filter(_.id === warningid)
      db.run(query.map(_.user).result).flatMap(_.head match {
        case user if user == account.userName => db.run(query.delete).map(_ => Ok("deleted"))
        case _ => Future(Unauthorized(s"${account.userName} is unauthorized to delete this warning"))
      })
    }
  }
}
