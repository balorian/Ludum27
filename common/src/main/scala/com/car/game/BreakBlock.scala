package com.car.game

import com.car.l.screens.LevelTestScreen
import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.g2d.Animation

class BreakBlock (var screen: LevelTestScreen, val maxHealth: Int) extends Entity(Map("idle" -> new Animation(1, assets.tileAtlas.findRegion("breakable_wall")),
																					  "damaged" -> new Animation(1, assets.tileAtlas.findRegion("breakable_wall_damaged"))) , 48, 0){
  var health = maxHealth
  
  override def act(delta: Float) {
    
    if(health <= maxHealth / 2){
      this.currentAnimation = "damaged"
    }
    
    if(health <= 0){
      remove
      screen.blockSet.remove(this)
    }
  }
  
  override def collides() = false
}