package com.nudemeth.example.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._


object WebServer extends App {
  private val server = WebServer()
  server.start()
  server.requete()
  StdIn.readLine() // let it run until user presses return
  server.stop()
}

case class User(name:String)

final case class WebServer() extends ServerRoutes {
  implicit val system: ActorSystem = ActorSystem("akka-http-react-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private var server: Future[Http.ServerBinding] = _

  implicit val session: Cluster = Cluster.builder
    .addContactPoint("127.0.0.1").withPort(9042)
    .build.connect()

  def start(): Unit = {
    server = Http().bindAndHandle(route, "localhost", 8080)
    log.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  }

  def requete(): Unit = {



    log.info("BOI");
  }

  def stop(): Unit = {
    server
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
