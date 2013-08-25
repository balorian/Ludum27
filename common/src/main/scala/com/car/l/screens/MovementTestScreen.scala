package com.car.l.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.car.l.Assets.assets
import com.car.l.Assets.assets.manager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.Gdx.graphics
import com.car.l.LudumGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL10
import com.car.ui.GameUI

class MovementTestScreen(game: LudumGame) extends AbstractScreen(game) {
//  lazy val ui = new GameUI()

  override def show() {
    stage.addActor(new Character)
  }

  override def render(delta: Float) {
    gl.glClearColor(0.25f,0.25f, 0.25f, 1);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    stage.act(delta)
    stage.draw()

//    ui.render(delta)
  }
}

class Character extends Actor {
  lazy val anim = new Animation(0.25f, assets.creatureAtlas.createSprites("main"), Animation.LOOP)
  var stateTime = 0f

  def Character() {
    this.setPosition(50, 50)
  }

  override def act(delta: Float) {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) { this.setX(this.getX() - 50 * delta) }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) { this.setX(this.getX() + 50 * delta) }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) { this.setY(this.getY() + 50 * delta) }
    if (Gdx.input.isKeyPressed(Input.Keys.S)) { this.setY(this.getY() - 50 * delta) }

    stateTime += delta
  }

  override def draw(batch: SpriteBatch, parentAlpha: Float) = {
    batch.draw(anim.getKeyFrame(stateTime), this.getX, this.getY)
  }
}