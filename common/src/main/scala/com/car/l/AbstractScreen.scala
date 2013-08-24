package com.car.l

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx.graphics

class AbstractScreen extends Screen {
  var paused = false
  var visible = false
  lazy val stage: Stage = new Stage(graphics.getWidth, graphics.getHeight,
    false)
  def show() {
    visible = true;
  }

  def hide() {
    visible = false;
  }

  def pause() {
    paused = true;
  }

  def resume() {
    paused = false;
  }

  def dispose(): Unit = ???

  def render(delta: Float): Unit = ???

  def resize(width: Int, height: Int): Unit = ???
}