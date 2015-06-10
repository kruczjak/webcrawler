package webcrawler

import org.htmlcleaner.TagNode

import scala.concurrent.Future
import scala.util.{Failure, Success}

object WebCrawler extends App {
  import scala.io.Source
  import scala.concurrent._
  import scala.io.StdIn.readInt
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  def operation(url: String, level: Int): Future[(Array[TagNode], Int)] = Future {
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(new URL(url))
    (rootNode.getElementsByName("a", true), level)
  }

  val maxLevel = readInt()
  val url = "http://google.com"
  val start = operation(url, maxLevel)

  start onComplete {
    case Success((elements, level)) =>
      elements map { elem =>
        val url = elem.getAttributeByName("href")
        println(url.toString)
        level match {
          case 0 =>
          case _ => operation(url.toString, level-1)
        }
      }
    case Failure(err) => println("Error: " + err.getMessage)
  }

  Thread.sleep(3000)

}
