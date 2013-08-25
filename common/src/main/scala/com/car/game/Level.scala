package com.car.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.MathUtils.floor

object Tile {
  val TILE_SIZE = 32

  val GROUND = 0xFFFFFFFF
  val WALL = 0x000000FF
  val WATER = 0x00FFFFFF
  val STAIRS_DOWN = 0xFF0000FF
  val STAIRS_UP = 0xFF00E1FF
  val EMPTY = 0x0000000

  val types = Map(GROUND -> "ground", WALL -> "wall", WATER -> "water", STAIRS_DOWN -> "stairs_down", STAIRS_UP -> "stairs_up")
}

class Level(val mapWidth: Int, val mapHeight: Int, tileMap: Array[Int]) extends Actor {
  //  this(width:Int, height:Int, map: Array[Int]){
  //    super()
  //  }

  val tileSheet = assets.tileAtlas

  def getTile(x: Int, y: Int): Int = if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) tileMap(x + mapWidth * y) else Tile.EMPTY

  override def act(delta: Float) {
  }

  def collides(x: Float, y: Float): Boolean = getTile(x.round, y.round) == Tile.WALL

  def collides(rect: Rectangle): Boolean = {
    val tlc = (rect.getX / Tile.TILE_SIZE, (rect.getY + rect.getHeight) / Tile.TILE_SIZE)
    val trc = ((rect.getX + rect.getWidth) / Tile.TILE_SIZE, (rect.getY + rect.getHeight) / Tile.TILE_SIZE)
    val blc = (rect.getX / Tile.TILE_SIZE, rect.getY / Tile.TILE_SIZE)
    val brc = ((rect.getX + rect.getWidth) / Tile.TILE_SIZE, rect.getY / Tile.TILE_SIZE)

    getTile(floor(blc._1), floor(blc._2)) == Tile.WALL || getTile(floor(brc._1), floor(brc._2)) == Tile.WALL ||
      getTile(floor(tlc._1), floor(tlc._2)) == Tile.WALL || getTile(floor(trc._1), floor(trc._2)) == Tile.WALL
  }

  override def draw(batch: SpriteBatch, parentAlpha: Float) {
    for (j <- 0 until mapHeight; i <- 0 until mapWidth) {
      batch.draw(tileSheet.findRegion(Tile.types(getTile(i, j))), i * Tile.TILE_SIZE, j * Tile.TILE_SIZE)
    }
  }
}