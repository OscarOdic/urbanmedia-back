package services

import com.typesafe.config.ConfigFactory
import models.Tables._
import models.Media
import slick.jdbc.PostgresProfile.backend.DatabaseDef
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MediaService {
  def getMediasFromPlace[T <: Medias](table: TableQuery[T], tableName: String)(placeId: Int)(db: DatabaseDef): Future[List[Media]] =
    db.run(table.filter(_.placeId === placeId).map(media => (media.author, media.id)).result).map(_.toList.map {
      case (author, mediaId) => Media(
        Some(mediaId),
        author,
        s"${ConfigFactory.load().getString("application.baseUrl")}place/$tableName?placeid=$placeId&${tableName}id=$mediaId"
      )
    })

  def getImagesFromPlace = getMediasFromPlace(images, "image") _

  def getSongsFromPlace = getMediasFromPlace(songs, "song") _

  def getVideosFromPlace = getMediasFromPlace(videos, "video") _
}
