package mimp.gui.draw

import scala.swing.Component
import scala.swing.Graphics2D
import scala.collection.immutable.TreeMap
import scala.collection.mutable.{ HashMap => MHashMap }

trait DrawingCommand {
  def draw(g: Graphics2D)
}

case class DrawingLayer(val drawing: (Graphics2D => Unit)) extends DrawingCommand {
  override def draw(g: Graphics2D) = drawing(g)
}

class Layers extends DrawingCommand {
  var layers = TreeMap[Int, DrawingLayer]()
  def add(depth: Int, cmd: DrawingLayer) = {
    this.layers = this.layers.+((depth, cmd))
  }
  def remove(depth: Int) = {
    this.layers = this.layers.-(depth)
  }
  def insert(depth: Int, cmd: DrawingLayer) = {
    this.layers = this.layers.map {
      case (d, c) if d < depth => (d, c)
      case (d, c) => (d+1, c)
    } + ((depth, cmd))
  }
  def swap(d1: Int, d2: Int) = {
    if (layers.contains(d1) && layers.contains(d2)) {
      this.layers = this.layers.map {
        case (d, c) if d == d1 => (d2, c)
        case (d, c) if d == d2 => (d1, c)
        case x => x
      }
    }
  }
  def top = this.layers.keys.lastOption.getOrElse(0)
  def bottom = this.layers.keys.headOption.getOrElse(0)

  override def draw(g: Graphics2D) = {
    for (depth <- layers.keys; cmd <- layers.get(depth)) {
      cmd.draw(g)
    }
  }
}

class CommandCanvas(val id: Int, val cmd: DrawingCommand) extends Component {
  override protected def paintComponent(g: Graphics2D) = {
    cmd.draw(g)
  }
}

object CommandCanvas {
  val canvas = new MHashMap[Int, CommandCanvas]()
  def get(id: Int): CommandCanvas = {
    val add = (id: Int) => {
      val c = new CommandCanvas(id, new Layers())
      canvas += ((id, c))
      c
    }
    canvas.getOrElse(id, add(id))
  }
  def getLayers(id: Int): Layers = {
    this.get(id).cmd match {
      case layers: Layers => layers
      case _ => throw new IllegalArgumentException("Invalid Creation Canvas Items.")
    }
  }
}