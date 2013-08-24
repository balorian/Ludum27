package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.MathUtils.floor

object Tile {
  val TILE_SIZE = 32
  val GROUND = 0
  val WALL = 1
  val WATER = 2
  val STAIRS_DOWN = 3
  val STAIRS_UP = 4
  val EMPTY = 5
  
  val types = Map (GROUND -> "ground", WALL -> "wall", WATER -> "water", STAIRS_DOWN -> "stairs_down", STAIRS_UP -> "stairs_up")
}

class Level extends Actor {
  val tileSheet = assets.tileAtlas
  var mapWidth = 10
  var mapHeight = 10
  var tileMap = new Array[Int](100)
  
  val g = Tile.GROUND
  val w = Tile.WALL
  
  tileMap = Array(w, w, w, w, w, w, w, w, w, w,
		  		  w, g, g, g, g, g, g, g, g, w,
		  		  w, g, g, g, g, g, g, g, g, w,
		  		  w, g, g, w, w, w, g, w, g, w,
		  		  w, w, g, g, g, w, g, g, g, w,
		  		  w, g, g, g, g, w, w, w, g, w,
		  		  w, g, g, g, g, g, g, g, g, w,
		  		  w, g, g, g, g, w, g, w, g, w,
		  		  w, g, g, g, g, w, g, g, g, w,
		  		  w, w, w, w, w, w, w, w, w, w)
  
  def getTile(x: Int, y: Int): Int = if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) tileMap(x + mapWidth * y) else Tile.EMPTY
  
  override def act(delta: Float) {
    
    
  }
  
  def collides(x: Float, y: Float): Boolean = getTile(x.round, y.round) == Tile.WALL
  
  def collides(rect: Rectangle): Boolean =  {
    val tlc = (rect.getX/Tile.TILE_SIZE, (rect.getY+rect.getHeight)/Tile.TILE_SIZE)
    val trc = ((rect.getX+rect.getWidth)/Tile.TILE_SIZE, (rect.getY+rect.getHeight)/Tile.TILE_SIZE)
    val blc = (rect.getX/Tile.TILE_SIZE, rect.getY/Tile.TILE_SIZE)
    val brc = ((rect.getX+rect.getWidth)/Tile.TILE_SIZE, rect.getY/Tile.TILE_SIZE)
        
    getTile(floor(blc._1), floor(blc._2)) == Tile.WALL || getTile(floor(brc._1), floor(brc._2)) == Tile.WALL ||
    getTile(floor(tlc._1), floor(tlc._2)) == Tile.WALL || getTile(floor(trc._1), floor(trc._2)) == Tile.WALL 
    }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float) {
    for( j <- 0 until mapHeight; i <- 0 until mapWidth )
      batch.draw(tileSheet.findRegion(Tile.types(getTile(i, j))), i * Tile.TILE_SIZE, j * Tile.TILE_SIZE)
  }
  
}