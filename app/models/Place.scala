package models

class Place(id: Option[Int], name: String)

case class GeoLocPlace(
                        id: Option[Int],
                        name: String,
                        latitude: Double,
                        longitude: Double,
                        whoAdded: String
                      ) extends Place(id, name)

case class DetailsPlace(
                         id: Option[Int],
                         name: String,
                         whoAdded: String,
                         comments: List[Reaction] = List.empty,
                         warnings: List[Reaction] = List.empty,
                         image: Option[String] = None
                       ) extends Place(id, name)