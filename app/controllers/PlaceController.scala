package controllers

import javax.inject._

import models.GeoLocPlace
import play.api.mvc._
import play.api.libs.json.Json
import utils.SlickDatabase
import models.Tables._
import services.{AuthService, PlaceService}
import slick.jdbc.PostgresProfile.api._
import utils.JsonFormatters._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PlaceController @Inject() extends Controller {

  def index(placeid: Int) = Action.async {
    val db = SlickDatabase.get
    PlaceService.getPlace(placeid)(db).map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound(s"Place with id $placeid not found")
    }
  }

  def randomBackgroundImage(placeid: Int) = Action.async {
    val db = SlickDatabase.get
    val rand = SimpleFunction.nullary[Double]("random")
    db.run(images.filter(_.placeId === placeid).sortBy(_ => rand).take(1).map(_.media).result.transactionally).map(_.headOption match {
      case Some(image) => Ok(image).as("image")
      case None => NotFound(s"Place with id $placeid not found")
    })
  }

  def add = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = DBIO.seq(
        geoLocPlaces += GeoLocPlace(
          None,
          request.body("name").head,
          request.body("latitude").head.toDouble,
          request.body("longitude").head.toDouble,
          account.userName
        )
      )
      db.run(insert).map(_ => Ok("added"))
    }
  }

  def followPlace = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      val insert = DBIO.seq(
        follow += (account.userName, request.body("placeid").head.toInt)
      )
      db.run(insert).map(_ => Ok("followed"))
    }
  }

  def unfollowPlace = Action.async(parse.urlFormEncoded) { request =>
    val db = SlickDatabase.get
    new AuthService(db).withAuth(request.headers) { account =>
      db.run(follow.filter(f => f.userName === account.userName && f.placeId === request.body("placeid").head.toInt).delete)
        .map(_ => Ok("unfollowed"))
    }
  }
}
