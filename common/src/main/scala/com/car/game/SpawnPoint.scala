package com.car.game

import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.graphics.g2d.Animation

object SpawnPoint {
  val SPAWN_TIME = 3f
}

class SpawnPoint(animations: Map[String, Animation], var screen: LevelTestScreen, val maxHealth: Int, val enemyType: Symbol, val id: Int) extends Entity(animations, 48, 0) {
  var stateTime: Float = 0
  var health = maxHealth
  
  override def act(delta: Float) {
    if(health <= maxHealth / 2){
      this.currentAnimation = "damaged"
    }
    
    stateTime += delta
    if (stateTime >= SpawnPoint.SPAWN_TIME) {
      stateTime = 0
      println("SPAWNER " + id + " ATTEMPTING TO SPAWN")
      screen.spawnOnPoint(this)
    }
    if(health <= 0){
      remove
      screen.spawnSet.remove(this)
    }
  }
  
  override def collides() = false
}