package controllers

import javax.inject._

import models.Account
import play.api.libs.json.Json
import play.api.mvc._
import services.{AuthService, UserService}
import utils.SlickDatabase
import models.Tables.accounts
import org.mindrot.jbcrypt.BCrypt
import slick.jdbc.PostgresProfile.api._
import utils.JsonFormatters._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserController @Inject() extends Controller {

  def index(username: String) = Action.async {
    val db = SlickDatabase.get
    UserService.getUser(username)(db).map {
      case Some(u) => Ok(Json.toJson(u))
      case None => NotFound(s"Place with userName $username not found")
    }
  }

  def create = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    val insert = DBIO.seq(
      accounts += Account(
        request.body("username").head,
        BCrypt.hashpw(request.body("password").head, BCrypt.gensalt()),
        request.body("bio").headOption
      )
    )
    db.run(insert).map(_ => Ok("added"))
  }

  def check = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    AuthService.check(
      request.body("username").head,
      request.body("password").head
    )(db).map {
      case Right(_) => true
      case Left(_) => false
    }.map(b => Ok(b.toString))
  }
}
