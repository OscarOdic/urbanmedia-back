package services

import com.typesafe.config.ConfigFactory
import models.Tables._
import models.Media
import slick.jdbc.PostgresProfile.backend.DatabaseDef
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MediaService {
  def getMediasFromPlace[T <: Medias](table: TableQuery[T], tableName: String, fileType: String)(placeId: Int)(db: DatabaseDef): Future[List[Media]] =
    db.run(table.filter(_.placeId === placeId).sortBy(_.id).map(media => (media.author, media.id)).result).map(_.toList.map {
      case (author, mediaId) => Media(
        Some(mediaId),
        author,
        s"${ConfigFactory.load().getString("application.baseUrl")}$tableName/$tableName$mediaId.$fileType"
      )
    })

  def getImagesFromPlace = getMediasFromPlace(images, "image", "png") _

  def getSongsFromPlace = getMediasFromPlace(songs, "song", "mp3") _

  def getVideosFromPlace = getMediasFromPlace(videos, "video", "mp4") _
}
