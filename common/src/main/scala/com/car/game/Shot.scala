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
    if(freeShots.isEmpty) new Shot(screen, assets.creatureAtlas.findRegion("axe", 1))
    else {
      val ret = freeShots.pop()
      ret.setVisible(true)
      screen.stage.addActor(ret)
      ret
    }
  }
  
  def returnShot(shot: Shot){
    shot.deltaV = new Vector2(0, 0)
    shot.spinCounter = 0
    shot.setVisible(false)
    shot.remove
    freeShots.push(shot)
  }
}

class Shot(screen: LevelTestScreen, image: TextureRegion) extends Actor{
  val damage = 7
  var deltaV = new Vector2(0, 0)
  var spinCounter = 0
  screen.stage.addActor(this)
  
  override def act(delta: Float){
    setPosition(getX + deltaV.x, getY + deltaV.y)
    
    screen.spawnSet.foreach(spoint => if (spoint.contains(getX, getY)) {spoint.health -= damage; ShotPool.returnShot(this)})
    
    if(screen.level.get.collidesWith(getX+image.getRegionWidth/2, getY+image.getRegionHeight/2, Tile.WALL)){
      ShotPool.returnShot(this)
    }
    spinCounter += 1
    setRotation(spinCounter * 45)
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float){
    val center = image.getRegionWidth/2
    batch.draw(image.getTexture, getX-center, getY-center, image.getRegionWidth/2, image.getRegionHeight/2, image.getRegionWidth, image.getRegionHeight, 1, 1,
    		 	getRotation, image.getRegionX, image.getRegionY, image.getRegionWidth, image.getRegionHeight, false, false)
  }
  
}