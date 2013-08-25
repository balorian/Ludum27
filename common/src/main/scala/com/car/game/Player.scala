package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap.Format
import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.utils.Pool

class PlayerProcessor(player: Player) extends InputProcessor {
  val moveKeys = Map(Keys.W -> 0, Keys.D -> 1, Keys.S -> 2, Keys.A -> 3)
  val shootKeys = Map(Keys.UP -> 1, Keys.RIGHT -> 2, Keys.DOWN -> 4, Keys.LEFT -> 8)

  override def keyDown(keycode: Int): Boolean = {
    if (moveKeys.contains(keycode)) player.movement(moveKeys(keycode)) = true
    if (shootKeys.contains(keycode)){
      player.shootDir += shootKeys(keycode)
      
    }
    true
  }

  override def keyUp(keycode: Int): Boolean = {
    if (moveKeys.contains(keycode)) player.movement(moveKeys(keycode)) = false
    if (shootKeys.contains(keycode)){
      player.shootDir -= shootKeys(keycode)
    }
    true
  }

  override def keyTyped(char: Char) = false
  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
  override def touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
  override def mouseMoved(screenX: Int, screenY: Int) = false
  override def scrolled(amount: Int) = false

}

class Player(animations: Map[String, Animation], var screen: LevelTestScreen) extends Entity(animations, 48, 7) {
  val SPEED = 5
  val WEAPON_COOLDOWN = 0.2f
  var movement = Array(false, false, false, false)
  var shootDir = 0
  var shootCooldown = 0f
  
  
  setPosition(4*48, 4*48)
  
  override def act(delta: Float) {
    if (movement(0) || movement(1) || movement(2) || movement(3)) swapAnimation("walk")
    else swapAnimation("idle")
    
    shootCooldown += delta
    
    super.act(delta)
    var deltaV = new Vector2(0, 0)
    if (movement(0)) {deltaV.add(0, 1); setRotation(0)}
    if (movement(2)) {deltaV.add(0, -1); setRotation(180)}
    if (movement(1)) {deltaV.add(1, 0); if(movement(0)) setRotation(315) else if(movement(2)) setRotation(225) else setRotation(270)}
    if (movement(3)) {deltaV.add(-1, 0); if(movement(0)) setRotation(45) else if(movement(2)) setRotation(135) else setRotation(90)}
    deltaV.nor().scl(SPEED)
    
    def scan(step: Float, dir: Vector2, length: Float) {
      val newLength = length - step
      val oldDir = new Vector2(dir.x, dir.y)
      if (length > 0) {
        val oldPos = (getX, getY)
        val scl = dir.scl(step)
        setPosition(scl.x + getX, scl.y + getY)
        if (screen.level.get.collidesWith(boundingBox, Tile.WALL) || screen.level.get.collidesWith(boundingBox, Tile.WATER))
          setPosition(oldPos._1, oldPos._2)
        else if (newLength < step)
          scan(newLength, oldDir, 0)
        else 
          scan(step, oldDir, newLength)
      }
    }

    scan(0.05f, new Vector2(deltaV.x, 0).nor, deltaV.len)
    scan(0.05f, new Vector2(0, deltaV.y).nor, deltaV.len)
    
    def spawnShot(dir: Int){
      val shot = ShotPool.getShot(screen)
      shot.setPosition(getX + 48/2, getY + 48/2)
      var shotV: Vector2 = new Vector2(0, 0)
      if((1 & shootDir) > 0) shotV.add(0, 1)
      if((2 & shootDir) > 0) shotV.add(1, 0)
      if((4 & shootDir) > 0) shotV.add(0, -1)
      if((8 & shootDir) > 0) shotV.add(-1, 0)
      shot.deltaV = shotV.nor.scl(SPEED*3)
    }
    
    if(shootDir > 0 && shootCooldown > WEAPON_COOLDOWN){
      spawnShot(shootDir)
      shootCooldown = 0
    }
  }

  override def draw(batch: SpriteBatch, parentAlpha: Float) {
    super.draw(batch, parentAlpha)
    val pixmap = new Pixmap(34, 34, Format.RGBA8888);
    pixmap.setColor(1, 0, 0, 1f);
    pixmap.drawRectangle(0, 0, 34, 34);
    val texmex = new Texture(pixmap);
    batch.draw(texmex, boundingBox.x, boundingBox.y)
  }

}