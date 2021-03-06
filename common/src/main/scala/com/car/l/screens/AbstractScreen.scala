package com.car.l.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.Gdx.gl
import com.car.l.LudumGame
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera

abstract class AbstractScreen(val game: LudumGame) extends Screen {
  var paused = false
  var visible = false
  var justDraw = false
  
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
  
  def into(){
    
  }
  
  def outOf(){
    
  }
  
  def dispose(): Unit = {
    stage.dispose()
  }

  def render(delta: Float): Unit = {
    gl.glClearColor(0, 0, 0, 1);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    stage.act(delta)
    stage.getCamera.asInstanceOf[OrthographicCamera].zoom = 0.75f
    stage.getCamera.update
    stage.draw()
  }

  def resize(width: Int, height: Int): Unit = {
    stage.setViewport(width, height, false)
  }
  
  def inputProcessor : InputProcessor = stage
}