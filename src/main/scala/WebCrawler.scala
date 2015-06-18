package webcrawler

import org.htmlcleaner.TagNode

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object WebCrawler extends App {
  import scala.io.Source
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.StdIn.readInt
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  def operate(url: String): Seq[String] = {
    try {
      val cleaner = new HtmlCleaner
      val props = cleaner.getProperties
      val rootNode = cleaner.clean(new URL(url))

      val nodes = rootNode.getElementsByName("a", true).map(_.getAttributeByName("href")).map {
        case ent if !(ent contains "http") => url + "/" + ent
        case ent => ent
      }
      nodes
    }
    catch {
      case _: Throwable => Seq()
    }
  }

  val maxLevel = 9
  val start = "http://galaxy.agh.edu.pl/~balis/dydakt"
  var urls = Seq(start)

  for (levelNow <- 1 to maxLevel) {
    val futureUrls = urls.map(url => Future { operate(url) } )
    val future = Future.sequence(futureUrls).map(_.flatten)

    urls = Await.result(future, 5 minutes)
    urls.foreach(url => println(levelNow + " " + url))
    urls = urls.filter(_ contains start)
  }

}
