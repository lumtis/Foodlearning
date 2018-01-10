package com.nudemeth.example.server

import com.datastax.driver.core._
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.{ FutureCallback, Futures, ListenableFuture }
import scala.concurrent.{ Future, Promise }
import scala.language.implicitConversions
import scala.concurrent.{ ExecutionContext, Future, Promise }

implicit class CqlStrings(val context: StringContext) extends AnyVal {
  def cql(args: Any*)(implicit session: Session): Future[PreparedStatement] = {
    val statement = new SimpleStatement(context.raw(args: _*))
    session.prepareAsync(statement)
  }
}


implicit def listenableFutureToFuture[T](
  listenableFuture: ListenableFuture[T]
): Future[T] = {
  val promise = Promise[T]()
  Futures.addCallback(listenableFuture, new FutureCallback[T] {
    def onFailure(error: Throwable): Unit = {
      promise.failure(error)
      ()
    }
    def onSuccess(result: T): Unit = {
      promise.success(result)
      ()
    }
  })
  promise.future
}


implicit val session = new Cluster
    .Builder()
    .addContactPoints("localhost")
    .withPort(9142)
    .build()
    .connect()


//a cassandra instance should be running on localhost at this point
//sudo service cassandra start


//val statement = cql"SELECT ing1, ing2, coef FROM pairs WHERE ing1 IN  "+ ingTab + "AND ing2 IN " + ingTab

def execute(statement: Future[PreparedStatement], params: Any*)(
  implicit executionContext: ExecutionContext, session: Session
): Future[ResultSet] =
  statement
    .map(_.bind(params.map(_.asInstanceOf[Object])))
    .flatMap(session.executeAsync(_))

//execution de la requete
val myKey = 3
val resultSet = execute(
   cql"SELECT ing1, ing2, coef FROM pairs WHERE ing1='Huile' AND ing2='Sel'",
   myKey
)

//works for a small number of results ONLY
val rows: Future[Iterable[Row]] = resultSet.map(_.asScala)

//parsing of the rows
val maybeIng1 =
  if (resultSet.isNull("ing1")) None
  else Some(resultSet.getString("ing1"))

val maybeIng2 =
  if (resultSet.isNull("ing2")) None
  else Some(resultSet.getString("ing2"))

val maybeCoef =
  if (resultSet.isNull("coef")) None
  else Some(resultSet.getFloat("coef"))
var coef = 0.0;
if(coef > 0.3{
  log.info(s"Un couple convient : "+maybeIng1+"--"+maybeIng2+" avec pour coefficient "+maybeCoef+")
}
