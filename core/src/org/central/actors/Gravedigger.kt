package org.central.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import org.central.AppObj

class Gravedigger: Actor() {

    val xpos = AppObj.stg.width - AppObj.stg.width + 20

    init {
        width = 140f
        height = 140f
    }

    var tex = TextureRegion(Texture(Gdx.files.internal("gravedigger.png")))
    val regions: Array<TextureRegion> = tex.split(200, 200)[0]
    var sheetTex = mutableMapOf("neutral" to regions[0],
            "happy" to regions[1],
            "diamond" to regions[2]
    )

    var currentTex = regions[0]

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(currentTex, xpos, AppObj.stg.height - height - 10, width, height)
    }
}