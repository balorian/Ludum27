package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Touchable

abstract class Entity(animations: Map[String, Animation]) extends Actor {
  var currentAnimation: String = "idle"
  var animationTimer: Float = 0
  
  override def hit(x: Float, y: Float, touchable: Boolean): Actor =
    if ((getTouchable == Touchable.enabled) && touchable && x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight())
      this else null
  
  override def act(delta: Float) = {
    super.act(delta)
    animationTimer += delta
  }
  
  def swapAnimation(anim: String) {
    animationTimer = 0
    currentAnimation = anim
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float) = {
    batch.draw(animations(currentAnimation).getKeyFrame(animationTimer), getX(), getY())
  }
}