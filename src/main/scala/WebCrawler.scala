package webcrawler

object WebCrawler extends App {
  import scala.io.Source
  import scala.io.StdIn.readInt
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  val maxLevel = readInt()
  val url = "http://google.com"



  val cleaner = new HtmlCleaner
  val rootNode = cleaner.clean(new URL(url))
  val elements = rootNode.getElementsByName("a", true) 
  elements map { elem => 
    val url = elem.getAttributeByName("href")
    println(url.toString) 
  }
}
