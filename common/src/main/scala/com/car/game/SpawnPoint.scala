package com.car.game

import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.graphics.g2d.Animation

object SpawnPoint {
  val SPAWN_TIME = 5f
}

class SpawnPoint(animations: Map[String, Animation], var screen: LevelTestScreen) extends Entity(animations, 48, 0) {
  var stateTime: Float = 0
  var health = 20
  
  override def act(delta: Float) {
    stateTime += delta
    if (stateTime >= SpawnPoint.SPAWN_TIME) {
      stateTime = 0
      screen.spawnOnPoint(this)
    }
    if(health <= 0){
      remove
      screen.spawnSet.remove(this)
    }
  }
}