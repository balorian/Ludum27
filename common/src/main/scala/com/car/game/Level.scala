package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.MathUtils.floor

object Tile {
  val TILE_SIZE = 48

  val GROUND = 0xFFFFFFFF
  val WALL = 0x000000FF
  val WATER = 0x00FFFFFF
  val STAIRS_DOWN = 0xFF0000FF
  val STAIRS_UP = 0xFF00E1FF
  val EMPTY = 0x0000000

  val types = Map(GROUND -> "ground", WALL -> "wall", WATER -> "water", STAIRS_DOWN -> "stairs_down", STAIRS_UP -> "stairs_up")
}

class Level(val mapWidth: Int, val mapHeight: Int, tileMap: Array[Int]) extends Actor {
  val tileSheet = assets.tileAtlas


  
  
  def getTile(x: Int, y: Int): Int = if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) tileMap(x + mapWidth * y) else Tile.EMPTY
  def unitToMap(pair: (Float, Float)) = (floor(pair._1/Tile.TILE_SIZE), floor(pair._2/Tile.TILE_SIZE))

  def collidesWith(x: Float, y: Float, tileType: Int): Boolean = {
    val mapCoord = unitToMap((x, y))
    getTile(mapCoord._1, mapCoord._2) == tileType
  }

  def collidesWith(rect: Rectangle, tileType: Int): Boolean =  {
    val tlc = unitToMap(rect.getX, rect.getY+rect.getHeight)
    val trc = unitToMap(rect.getX+rect.getWidth, (rect.getY+rect.getHeight))
    val blc = unitToMap(rect.getX, rect.getY)
    val brc = unitToMap(rect.getX+rect.getWidth, rect.getY)

    getTile(blc._1, blc._2) == tileType || getTile(brc._1, brc._2) == tileType ||
    getTile(tlc._1, tlc._2) == tileType || getTile(trc._1, trc._2) == tileType 
  }

  override def draw(batch: SpriteBatch, parentAlpha: Float) {
    for (j <- 0 until mapHeight; i <- 0 until mapWidth) {
      batch.draw(tileSheet.findRegion(Tile.types(getTile(i, j))), i * Tile.TILE_SIZE, j * Tile.TILE_SIZE)
    }
  }
}