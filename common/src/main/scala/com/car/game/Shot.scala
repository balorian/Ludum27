package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool.Poolable
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.car.l.Assets.assets
import scala.collection.mutable.Stack

object ShotPool{
  var freeShots: Stack[Shot] = new Stack[Shot]()
  
  def getShot(screen: LevelTestScreen): Shot = {
    println("RETURNING A SHOT: " + freeShots.size)
    if(freeShots.isEmpty) new Shot(screen, assets.creatureAtlas.findRegion("sword", 1))
    else {
      val ret = freeShots.pop()
      ret.setVisible(true)
      screen.stage.addActor(ret)
      ret
    }
  }
  
  def returnShot(shot: Shot){
    shot.deltaV = new Vector2(0, 0)
    freeShots.push(shot)
  }
}

class Shot(screen: LevelTestScreen, image: TextureRegion) extends Actor{
  var deltaV = new Vector2(0, 0)
  screen.stage.addActor(this)
  
  override def act(delta: Float){
    setPosition(getX + deltaV.x, getY + deltaV.y)
    
    val mapLoc = screen.level.get.unitToMap((getX+image.getRegionWidth/2, getY+image.getRegionHeight/2))
    
    if(screen.level.get.collidesWith(getX+image.getRegionWidth/2, getY+image.getRegionHeight/2, Tile.WALL)){
      setVisible(false)
      remove
      ShotPool.returnShot(this)
    }
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float){
    batch.draw(image, getX, getY)
  }
  
}