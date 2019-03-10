package org.central.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import org.central.AppObj


class InputCtl {
    var lPressed = false
    var rPressed = false
    var aPressed = false
    var bPressed = false

    init {
        AppObj.hudStg.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent, keycode: Int): Boolean {
                if (keycode == Input.Keys.SPACE) {
                    aPressed = true
                }
                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
                    lPressed = true
                    rPressed = false
                } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
                    lPressed = false
                    rPressed = true
                }
                return false
            }

            override fun keyUp(event: InputEvent, keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.SPACE -> {
                        aPressed = false
                    }
                    Input.Keys.LEFT, Input.Keys.A -> {
                        lPressed = false
                    }
                    Input.Keys.RIGHT, Input.Keys.D -> {
                        rPressed = false
                    }
                }
                return false
            }

//            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
//                if (AppObj.dialogMode) aPressed = true
//                return false
//            }
//
//            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
//                if (AppObj.dialogMode) aPressed = false
//            }
        })
    }
}