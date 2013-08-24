package com.car.l.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.car.l.Assets.assets
import com.car.l.Assets.assets.manager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.Gdx.graphics
import com.car.l.LudumGame
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class MovementTestScreen(game: LudumGame) extends AbstractScreen(game) {
  override def show() {
    stage.addActor(new Character)
  }
}

class Character extends Actor {
  lazy val anim = new Animation(0.25f, assets.creatureAtlas.createSprites("main"), Animation.LOOP)
  var stateTime = 0f

  def Character() {
    this.setPosition(50, 50)
  }

  override def act(delta: Float) {
    if(Gdx.input.isKeyPressed(Input.Keys.A)){this.setX(this.getX() - 10 * delta)}
    if(Gdx.input.isKeyPressed(Input.Keys.D)){this.setX(this.getX() + 10* delta)}
    if(Gdx.input.isKeyPressed(Input.Keys.W)){this.setY(this.getY() + 10* delta)}
    if(Gdx.input.isKeyPressed(Input.Keys.S)){this.setY(this.getY() - 10* delta)}
    
    stateTime += delta
  }

  override def draw(batch: SpriteBatch, parentAlpha: Float) = {
    batch.draw(anim.getKeyFrame(stateTime), this.getX, this.getY)
  }
}