package com.car.game

import com.car.l.screens.LevelTestScreen
import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.g2d.Animation
import scala.math.random

class BreakBlock (var screen: LevelTestScreen, val maxHealth: Int) extends Entity(Map("idle" -> new Animation(1, assets.tileAtlas.findRegion("breakable_wall")),
																					  "damaged" -> new Animation(1, assets.tileAtlas.findRegion("breakable_wall_damaged"))) , 48, 0){
  var health = maxHealth
  
  override def act(delta: Float) {
    
    if(health <= maxHealth / 2){
      this.currentAnimation = "damaged"
    }
    
    if(health <= 0){
      remove
      if(random.toFloat < 0.05f) screen.createMeat(getX, getY)
      screen.blockSet.remove(this)
    }
  }
  
  override def collides() = false
}