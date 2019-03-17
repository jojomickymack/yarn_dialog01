package org.central.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import org.central.AppObj


class Damsel: Actor() {
    val xpos = AppObj.stg.width - 160f
    var tex = TextureRegion(Texture(Gdx.files.internal("damsel.png")))
    val regions = tex.split(200, 200)[0]

    var animations = mutableMapOf("static_normal" to Animation(0f, regions[0]),
            "static_angry" to Animation(0f, regions[1]),
            "static_shovel" to Animation(0f, regions[2]),
            "talk_normal" to Animation(0.15f, regions[3], regions[4], regions[5]),
            "talk_intrigued" to Animation(0.15f, regions[6], regions[7], regions[8]),
            "talk_disgusted" to Animation(0.15f, regions[9], regions[10], regions[11])
    )

    private var stateTime = 0f

    init {
        width = 140f
        height = 140f

        animations.forEach { k, v ->
            v.playMode = Animation.PlayMode.LOOP_PINGPONG
        }
    }

    var currentAnimation = animations["static_normal"]

    override fun act(delta: Float) {
        var deltaTime = delta
        if (deltaTime == 0f) return

        if (deltaTime > 0.1f)
            deltaTime = 0.1f

        this.stateTime += deltaTime
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(currentAnimation?.getKeyFrame(this.stateTime, true), xpos, AppObj.stg.height - height - 10, width, height)
    }
}