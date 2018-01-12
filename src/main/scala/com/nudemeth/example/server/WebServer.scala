package com.nudemeth.example.server

import akka.actor.ActorSystem
import akka.actor.ActorLogging
import akka.stream.ActorMaterializer
import akka.stream.alpakka.cassandra.scaladsl.CassandraSource
import akka.http.scaladsl.Http
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn
//import akka.cluster._
//import akka.cluster.ClusterEvent._
import com.typesafe.config.ConfigFactory
import com.datastax.driver.core.SimpleStatement
import com.datastax.driver.core.Cluster
import akka.stream.scaladsl.Sink

object WebServer extends App {
  private val server = WebServer()
  server.start()
  server.requete()
  StdIn.readLine() // let it run until user presses return
  server.stop()
}

final case class WebServer() extends ServerRoutes {
  implicit val system: ActorSystem = ActorSystem("akka-http-react-system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private var server: Future[Http.ServerBinding] = _

  implicit val session = Cluster.builder.addContactPoint("127.0.0.1").withPort(9042).build.connect()



  def start(): Unit = {
    server = Http().bindAndHandle(route, "localhost", 8080)
    log.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  }

  def requete(): Unit = {
    val stmt1 = new SimpleStatement("USE \"foodlearning\"").setFetchSize(20)
    CassandraSource(stmt1).runWith(Sink.seq)
    val stmt2 = new SimpleStatement("SELECT * FROM pairs").setFetchSize(20)
    val rows = CassandraSource(stmt2).runWith(Sink.seq)
    rows.get(0).onSuccess({
      case x=>{
        log.info("\n")
        log.info(s"result => ${x}\n")
      }
    })

  }

  def stop(): Unit = {
    server
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
