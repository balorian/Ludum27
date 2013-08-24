package com.car.l

import com.badlogic.gdx.Screen

class AbstractScreen extends Screen {
  var paused = false
  var visible = false

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