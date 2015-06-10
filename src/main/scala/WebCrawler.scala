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

  def operation(url: String, level: Int, maxLevel: Int): Future[(Array[TagNode], Int, Int)] = Future {
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(new URL(url))
    (rootNode.getElementsByName("a", true), level, maxLevel)
  }

  val maxLevel = readInt()
  val url = "http://google.com"
  val start = operation(url, 0, maxLevel)

  start onComplete {
    case Success((elements, level, maxLevel)) => {

    }
    case Failure(err) => println("Error: " + err.getMessage)
  }

//  val cleaner = new HtmlCleaner
//  val rootNode = cleaner.clean(new URL(url))
//  val elements = rootNode.getElementsByName("a", true)
//  elements map { elem =>
//    val url = elem.getAttributeByName("href")
//    println(url.toString)
//  }

  Await.result()

}
