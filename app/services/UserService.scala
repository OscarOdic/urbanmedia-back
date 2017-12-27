package services

import models.{Account, User}
import models.Tables._
import slick.jdbc.PostgresProfile.backend.DatabaseDef
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object UserService {
  def getUserFromAccount(account: Account)(db: DatabaseDef): Future[User] =
    db.run(follow.filter(_.userName === account.userName).join(geoLocPlaces).on((f, g) => f.placeId === g.id).map(_._2).result).map( places =>
      User(account.userName, account.bio, places.toList)
    )

  def getUser(userName: String)(db: DatabaseDef): Future[Option[User]] =
    db.run(accounts.filter(_.userName === userName).result).flatMap(_.headOption match {
      case Some(a) => getUserFromAccount(a)(db).map(u => Some(u))
      case None => Future(None)
    })
}
