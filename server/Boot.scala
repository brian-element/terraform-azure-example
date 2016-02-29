package com.elementanalytics.terraform_azure_example

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import spray.can.Http
import akka.actor._
import spray.routing._

object ServiceActor {
  def props = Props[ServiceActor]
}

class ServiceActor extends HttpServiceActor with Actor
{
  def receive = runRoute((pathSingleSlash & get)(complete("Hello, world!")))
}

object Boot extends App {
  implicit val system = ActorSystem("terraform-azure-example")
  
  val api = system.actorOf(Props(new ServiceActor()), "service-actor")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(api, interface = "0.0.0.0", port = 5000)
}
