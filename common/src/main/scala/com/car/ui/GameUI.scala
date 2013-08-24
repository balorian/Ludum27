package com.car.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color

class GameUI {
  lazy val stage: Stage = new Stage(graphics.getWidth, graphics.getHeight,
    false)

  val spiritBar = new StateBar(10, 10, 64, graphics.getHeight() - 20, new Color(1f, 0xFF / 0x13, 0xFF / 0x7F, 1))
  val healthBar = new StateBar(graphics.getWidth() - 74, 10, 64, graphics.getHeight() - 20, new Color(0.75f, 0, 0, 1))

  stage.addActor(spiritBar)
  stage.addActor(healthBar)

  def update() {

  }

  def render(delta: Float) {
    stage.act(delta)
    stage.draw()
  }
}