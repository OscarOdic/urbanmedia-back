package controllers

import javax.inject._

import play.api.mvc._

@Singleton
class Application @Inject() extends Controller {

  def index = Action {
    Ok("Hello world !")
  }

  def redirectDocs = Action {
    Redirect(url = "/assets/lib/swagger-ui/index.html", queryString = Map("url" -> Seq("/swagger.json")))
  }
}
