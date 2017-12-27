package services

import models.Account
import play.api.mvc._
import slick.jdbc.PostgresProfile.backend.DatabaseDef
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import models.Tables.accounts
import org.mindrot.jbcrypt.BCrypt
import scala.concurrent.ExecutionContext.Implicits.global

class AuthService(db: DatabaseDef) {
  def withAuth[A](headers: Headers)(f: Account => Future[Result]): Future[Result] = {
    val result = headers.get("Authorization") map { authHeader =>
      val (user, pass) = AuthService.decodeBasicAuth(authHeader)
      AuthService.check(user, pass)(db).flatMap {
        case Right(account) => f(account)
        case Left(e) => Future(e)
      }
    }
    result.getOrElse(Future(AuthService.unauthorized))
  }
}

object AuthService {
  def check(user: String, pass: String)(db: DatabaseDef): Future[Either[Result, Account]] =
    db.run(accounts.filter(_.userName === user).result).map(_.headOption match {
      case Some(a) if BCrypt.checkpw(pass, a.password) => Right(a)
      case _ => Left(AuthService.unauthorized)
    })

  private val unauthorized =
    Results.Unauthorized.withHeaders("WWW-Authenticate" -> "Basic realm=Unauthorized")

  private def decodeBasicAuth(authHeader: String): (String, String) = {
    val baStr = authHeader.replaceFirst("Basic ", "")
    val decoded = new sun.misc.BASE64Decoder().decodeBuffer(baStr)
    val Array(user, password) = new String(decoded).split(":")
    (user, password)
  }
}