package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2

class Player(animations: Map[String, Animation]) extends Entity(animations) {
  val speed = 100
  var movement = Array(false, false, false, false)
  var shootDir = 0
  var shootFlag = false
  
  val moveKeys = Map(Keys.W -> 0, Keys.D -> 1, Keys.S -> 2, Keys.A -> 3)
  val shootKeys = Map(Keys.UP -> 0, Keys.RIGHT -> 1, Keys.DOWN -> 2, Keys.LEFT -> 3)

  this.addListener(new InputListener() {
    override def keyDown(event: InputEvent, keycode: Int): Boolean = {
      println("KEY DOWN: " + keycode)
      if (moveKeys.contains(keycode)) movement(moveKeys(keycode)) = true
      true
    }

    override def keyUp(event: InputEvent, keycode: Int): Boolean = {
      if (moveKeys.contains(keycode)) movement(moveKeys(keycode)) = false
      true
    }})
    
  override def act(delta: Float) {
    var deltaV = Vector2.Zero
    if(movement(0)) deltaV.add(0, 1)
    if(movement(1)) deltaV.add(1, 0)
    if(movement(2)) deltaV.add(0, -1)
    if(movement(3)) deltaV.add(-1, 0)
    deltaV.add(1, 1)
    deltaV.nor().scl(speed)
    
    setPosition(getX() + deltaV.x, getY() + deltaV.y)
    //println("X: " + getX() + "  Y: " + getY())
  }

}