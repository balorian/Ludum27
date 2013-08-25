package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.math.Rectangle

abstract class Entity(animations: Map[String, Animation], entitySize: Int, boxOffset: Int) extends Actor {
  var currentAnimation: String = "idle"
  var animationTimer: Float = 0
  val boundingBox = new Rectangle(boxOffset, boxOffset, entitySize-2*boxOffset, entitySize-2*boxOffset)
  
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
  
  override def setPosition(x: Float, y: Float) = {
    super.setPosition(x, y)
    updateBoundingBox
  }
  
  def updateBoundingBox() = boundingBox.setPosition(getX+boxOffset, getY+boxOffset)
  
  def collidesWith(that: Entity) = boundingBox.overlaps(that.boundingBox)
  
  override def draw(batch: SpriteBatch, parentAlpha: Float) = {
    batch.draw(animations(currentAnimation).getKeyFrame(animationTimer), getX, getY)
    //batch.draw(animations(currentAnimation).getKeyFrame(animationTimer), getX, getY, entitySize/2, entitySize/2, entitySize, entitySize, 1, 1,
    	//	 	getRotation, 0, 0, entitySize, entitySize, false, false)
  }
}