package com.car.l

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen

class LudumGame extends Game {
  lazy val splashScreen = new SplashScreen()
  
  override def create() {
    splashScreen.create
    splashScreen.resize(Gdx.graphics.getWidth(),
      Gdx.graphics.getHeight())
    this.setScreen(splashScreen)
  }

  override def render() {
    splashScreen.render(Gdx.graphics.getDeltaTime)
  }

  override def resize(width: Int, height: Int) {
    super.resize(width, height);
  }
}
