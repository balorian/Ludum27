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
  
  def getShot(screen: LevelTestScreen, power: Boolean): Shot = {
    if(freeShots.isEmpty) new Shot(screen, (assets.creatureAtlas.findRegion("power_axe", 1), assets.creatureAtlas.findRegion("power_axe", 1)), power)
    else {
      val ret = freeShots.pop()
      ret.powered = power
      ret.setVisible(true)
      screen.stage.addActor(ret)
      ret
    }
  }
  
  def returnShot(shot: Shot){
    shot.deltaV = new Vector2(0, 0)
    shot.powered = false
    shot.spinCounter = 0
    shot.setVisible(false)
    shot.remove
    freeShots.push(shot)
  }
}

class Shot(screen: LevelTestScreen, images: (TextureRegion, TextureRegion), var powered: Boolean) extends Actor{
  val damage = 7
  var deltaV = new Vector2(0, 0)
  var spinCounter = 0
  screen.stage.addActor(this)
  
  override def act(delta: Float){
    setPosition(getX + deltaV.x, getY + deltaV.y)
    
    screen.enemySet.foreach(enemy => if (enemy.contains(getX, getY)) {enemy.health -= damage + 3; assets.playSound("hit"); ShotPool.returnShot(this); screen.player.score += 5})
    screen.spawnSet.foreach(spoint => if (spoint.contains(getX, getY)) {spoint.health -= damage + 3; assets.hit.play(); ShotPool.returnShot(this); screen.player.score += 5})
    screen.blockSet.foreach(block => if (block.contains(getX, getY)) {block.health -= damage + 3; assets.hit.play(); ShotPool.returnShot(this)})
    screen.doorSet.foreach(door => if (door.contains(getX, getY)) {ShotPool.returnShot(this)})
    
    if(screen.level.get.collidesWith(getX+images._1.getRegionWidth/2, getY+images._1.getRegionHeight/2, Tile.WALL)){
      ShotPool.returnShot(this)
    }
    spinCounter += 1
    setRotation(spinCounter * 45)
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float){
    val center = images._1.getRegionWidth/2
    val region = if(powered) images._2.getTexture else images._1.getTexture
    println(powered + "  " + region + " " + images._2.getTexture + " " + images._1.getTexture)
    batch.draw(region, getX-center, getY-center, images._1.getRegionWidth/2, images._1.getRegionHeight/2, images._1.getRegionWidth, images._1.getRegionHeight, 1, 1,
    		 	getRotation, images._1.getRegionX, images._1.getRegionY, images._1.getRegionWidth, images._1.getRegionHeight, false, false)
  }
  
}