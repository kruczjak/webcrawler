package webcrawler

import org.htmlcleaner.TagNode

import scala.concurrent.Future
import scala.util.{Failure, Success}

object WebCrawler extends App {
  import scala.io.Source
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import scala.io.StdIn.readInt
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  def operation(url: String, level: Int, list: List[String]): Future[(Array[TagNode], Int, List[String])] = Future {
    list :+ url
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(new URL(url))
    (rootNode.getElementsByName("a", true), level, list)
  }

//  val maxLevel = readInt()
  val maxLevel = 5
  val url = "http://interia.pl"
  val start = operation(url, maxLevel, List[String]())

  def ok(elements: Array[TagNode], level:Int, list: List[String]): Unit = {
    elements foreach { elem =>
      val url = elem.getAttributeByName("href")
      println(maxLevel - level + " " + url.toString)
      level match {
        case 0 =>
        case _ =>
          list.contains(url.toString) match {
            case false =>
              val f = operation(url.toString, level-1, list)
              f onComplete {
                case Success((a, b, l)) => ok(a, b, l)
                case Failure(err) =>  println("Error: " + err.getMessage)
              }
            case true =>
          }
      }
    }
  }

  start onComplete {
    case Success((a, b, l)) => ok(a, b, l)
    case Failure(err) =>  println("Error: " + err.getMessage)
  }

  Thread.sleep(990000)

}
