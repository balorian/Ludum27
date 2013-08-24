package com.car.l

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.Application

class LudumGame extends Game {

  override def create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG)
    val splashScreen = new SplashScreen
    splashScreen.create
    splashScreen.resize(Gdx.graphics.getWidth(),
      Gdx.graphics.getHeight())
    this.setScreen(splashScreen)
  }

}
