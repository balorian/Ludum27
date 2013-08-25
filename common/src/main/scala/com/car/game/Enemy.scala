package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.math.Vector2

class Enemy(animations: Map[String, Animation], var screen: LevelTestScreen) extends Entity(animations, 48, 7) {
  override def act(delta: Float) {
    val px = screen.player.getX().toInt
    val py = screen.player.getY().toInt

    val x = getX.toInt
    val y = getY.toInt

    val l = if (px < x) true else false
    val r = if (px > x) true else false
    val d = if (py > y) true else false
    val u = if (py < y) true else false

    var coord = (screen.level.get.getTile(x, y))

    (l, u, r, d) match {
      case (true, false, false, false) => coord = (screen.level.get.getTile(x - 1, y))
      case (true, true, false, false) => coord = (screen.level.get.getTile(x - 1, y - 1))
      case (true, false, false, true) => coord = (screen.level.get.getTile(x - 1, y + 1))
      case (false, false, true, false) => coord = (screen.level.get.getTile(x + 1, y))
      case (false, true, true, false) => coord = (screen.level.get.getTile(x + 1, y - 1))
      case (false, false, true, true) => coord = (screen.level.get.getTile(x + 1, y + 1))
      case (false, true, false, false) => coord = (screen.level.get.getTile(x, y + 1))
      case (false, false, false, true) => coord = (screen.level.get.getTile(x, y - 1))
      case _ =>
    }

    val angle: Float = Math.toDegrees(Math.atan2(px - x, py - y)).toFloat + 270
    setRotation(if (angle < 0) { angle + 360 } else angle)
    
    val displacement = new Vector2(Math.sin(angle).toFloat * 10, Math.cos(angle).toFloat * 10)
    this.setPosition(x + displacement.x, y + displacement.y)
    
    
  }
}