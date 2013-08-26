package com.car.game

import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.graphics.g2d.Animation
import scala.util.Random

object SpawnPoint {
  val SPAWN_TIME = 3f
  val random = new Random

  def generateNext() = { if (random.nextBoolean()) (SPAWN_TIME + random.nextFloat) else { SPAWN_TIME - random.nextFloat } }
}

class SpawnPoint(animations: Map[String, Animation], var screen: LevelTestScreen, val maxHealth: Int, val enemyType: Symbol) extends Entity(animations, 48, 0) {
  var stateTime: Float = SpawnPoint.generateNext
  var health = maxHealth

  override def act(delta: Float) {
    if (health <= maxHealth / 2) {
      this.currentAnimation = "damaged"
    }

    stateTime -= delta
    if (stateTime < 0) {
      stateTime = SpawnPoint.generateNext
      screen.spawnOnPoint(this)
    }
    if (health <= 0) {
      remove
      screen.spawnSet.remove(this)
      if (SpawnPoint.random.nextFloat < 0.2) screen.createMeat(getX, getY)
      else screen.createSoulShard(getX, getY)

      if (enemyType == 'skeleton) screen.player.score += 50
      else if (enemyType == 'zombie) screen.player.score += 75
      else screen.player.score += 100
    }
  }

  override def collides() = false
}