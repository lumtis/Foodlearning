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
import scala.collection.mutable.ListBuffer

object WebServer extends App {
  private val server = WebServer()
  server.start()
  server.requete("Huile", "Citron", "Sel", "Poivre", "Riz")
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

  def requete(ingredients: String*): Unit = {
    var listeIngredients = new ListBuffer[String]()

    ingredients.foreach(listeIngredients += _)

    val liste2ingredients = listeIngredients.toList
    val stmt1 = new SimpleStatement("USE \"foodlearning\"").setFetchSize(20)
    //val stmt2 = new SimpleStatement("SELECT * FROM pairs WHERE ing1='"+liste2ingredients(0)+"' AND ing2='"+liste2ingredients(1)+"'").setFetchSize(20)

    CassandraSource(stmt1).runWith(Sink.seq)

    for(ingcur <- liste2ingredients)
      for(ingcur2 <- liste2ingredients) {
        if(ingcur.toLowerCase != ingcur2.toLowerCase) {
          //println(ingcur + " - " + ingcur2 )
          val requetecur= "SELECT * FROM pairs WHERE ing1='"+ingcur+"' AND ing2='"+ingcur2+"'"
          println(requetecur)

          val rows = CassandraSource(new SimpleStatement(requetecur).setFetchSize(10)).runWith(Sink.seq)
          /*rows.onSuccess({
            case x=>{
              log.info("\n")
              log.info(s"result => ${x}\n")
            }
          })

          case rows => {
            println(rows)
            for (row <- rows) println(row.getString("name"))
            for (row <- rows) returnList += row.getString("name")
            println("ReturnList: " + returnList.mkString)
          }*/

          rows.onSuccess({
            case x=>{
              for (row <- x) {
                if(row.getFloat("coef")>0.2){
                  print(s"Une association convient => ${row}\n")
                }
              }
            }
          })

        }
      }

  }

  def stop(): Unit = {
    server
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
