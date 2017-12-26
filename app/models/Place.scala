package models

class Place(id: Int, name: String)

case class GeoLocPlace(
                        id: Int,
                        name: String,
                        latitude: Double,
                        longitude: Double
                      ) extends Place(id, name)

case class DetailsPlace(
                         id: Int,
                         name: String,
                         comments: List[Reaction] = List.empty,
                         warnings: List[Reaction] = List.empty
                       ) extends Place(id, name)