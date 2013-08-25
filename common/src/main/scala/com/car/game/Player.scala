package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap.Format

class PlayerProcessor(player: Player) extends InputProcessor {
  val moveKeys = Map(Keys.W -> 0, Keys.D -> 1, Keys.S -> 2, Keys.A -> 3)
  val shootKeys = Map(Keys.UP -> 0, Keys.RIGHT -> 1, Keys.DOWN -> 2, Keys.LEFT -> 3)
  
  override def keyDown(keycode: Int): Boolean = {
    if (moveKeys.contains(keycode)) player.movement(moveKeys(keycode)) = true
    true
  }

  override def keyUp(keycode: Int): Boolean = {
    if (moveKeys.contains(keycode)) player.movement(moveKeys(keycode)) = false
    true
  }
  
  override def keyTyped (char: Char) = false
  override def touchDown (screenX: Int, screenY: Int, pointer: Int, button: Int) = false
  override def touchUp (screenX: Int, screenY: Int, pointer: Int, button: Int) = false
  override def touchDragged (screenX: Int, screenY: Int, pointer: Int) = false
  override def mouseMoved (screenX: Int, screenY: Int) = false
  override def scrolled (amount: Int) = false
  
}

class Player(animations: Map[String, Animation]) extends Entity(animations) {
  val SPEED = 5
  val PLAYER_SIZE = 32
  val BOX_OFFSET = 5
  val boundingBox = new Rectangle(BOX_OFFSET, BOX_OFFSET, PLAYER_SIZE-BOX_OFFSET, PLAYER_SIZE-BOX_OFFSET)
  var movement = Array(false, false, false, false)
  var shootDir = 0
  var shootFlag = false
  
  setPosition(64, 64)
    
  override def act(delta: Float) {
    super.act(delta)
    
    var deltaV = new Vector2(0, 0)
    if(movement(0)) deltaV.add(0, 1)
    if(movement(1)) deltaV.add(1, 0)
    if(movement(2)) deltaV.add(0, -1)
    if(movement(3)) deltaV.add(-1, 0)
    deltaV.nor().scl(SPEED)
        
    setPosition(getX + deltaV.x, getY + deltaV.y)
    boundingBox.set(getX + BOX_OFFSET, getY + BOX_OFFSET, PLAYER_SIZE - 2 * BOX_OFFSET, PLAYER_SIZE - 2 * BOX_OFFSET)
    
//    if(level.collides(boundingBox)){
//      println("COLLIDES")
//    }
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float){
    super.draw(batch, parentAlpha)
    val pixmap = new Pixmap(22, 22, Format.RGBA8888);
    pixmap.setColor(1, 0, 0, 1f);
    pixmap.drawRectangle(0, 0, 22, 22);
    val texmex = new Texture(pixmap);
    batch.draw(texmex, boundingBox.x, boundingBox.y)
  }

}