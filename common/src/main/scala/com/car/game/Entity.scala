package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture

abstract class Entity(animations: Map[String, Animation], entitySize: Int, boxOffset: Int) extends Actor {
  var currentAnimation: String = "idle"
  var animationTimer: Float = 0
  var collisionPartner: Option[Entity] = None
  
  val boundingBox = new Rectangle(boxOffset, boxOffset, entitySize-2*boxOffset, entitySize-2*boxOffset)
  
  override def hit(x: Float, y: Float, touchable: Boolean): Actor =
    if ((getTouchable == Touchable.enabled) && touchable && x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight())
      this else null
  
  override def act(delta: Float) = {
    super.act(delta)
    animationTimer += delta
  }
  
  def swapAnimation(anim: String) {
    if(anim != currentAnimation){
      animationTimer = 0
      currentAnimation = anim
    }
  }
  
  override def setPosition(x: Float, y: Float) = {
    super.setPosition(x, y)
    updateBoundingBox
  }
  
  def updateBoundingBox() = boundingBox.setPosition(getX+boxOffset, getY+boxOffset)
  def getCenter() = (getX+entitySize/2, getY+entitySize/2)
  
  def contains(x: Float, y: Float) = boundingBox.contains(x, y)
  def collidesWith(that: Entity) = boundingBox.overlaps(that.boundingBox)
  def collidedWith(that: Entity) = collisionPartner = Some(that)
  
  def scan(step: Float, dir: Vector2, length: Float) {

      val ox = getX; val oy = getY
      setPosition(getX+dir.x*length, getY+dir.y*length)
      if(collides) {
        setPosition(ox, oy)
        fineScan(step, dir, length)
      }
      
      def fineScan(step: Float, dir: Vector2, length: Float) {
        val newLength = length - step
        val oldDir = new Vector2(dir.x, dir.y)
        if (length > 0) {
          val oldPos = (getX, getY)
          val scl = dir.scl(step)
          setPosition(scl.x + getX, scl.y + getY)

          if (collides)
            setPosition(oldPos._1, oldPos._2)
          else if (newLength < step)
            fineScan(newLength, oldDir, 0)
          else
            fineScan(step, oldDir, newLength)
        }
      }
    }
  
  def collides(): Boolean
  
  override def draw(batch: SpriteBatch, parentAlpha: Float) = {
    //batch.draw(animations(currentAnimation).getKeyFrame(animationTimer), getX, getY)
    val region = animations(currentAnimation).getKeyFrame(animationTimer)
    
    batch.draw(region.getTexture, getX, getY, entitySize/2, entitySize/2, entitySize, entitySize, 1, 1,
    		 	getRotation, region.getRegionX, region.getRegionY, entitySize, entitySize, false, false)
  }
}