package org.central

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import org.central.input.GamepadCtl
import org.central.input.InputCtl
import org.central.input.OnScreenGamepad
import ktx.scene2d.Scene2DSkin


object AppObj {

    var dialogMode = true
    var debug = false

    var width = Gdx.graphics.height.toFloat()
    var height = Gdx.graphics.width.toFloat()

    val sb = SpriteBatch()
    val cam = OrthographicCamera(width, height)
    val view = StretchViewport(480f, 360f, cam)
    val stg = Stage(view, sb)

    val hudSb = SpriteBatch()
    val hudCam = OrthographicCamera(width, height)
    val hudView = StretchViewport(480f, 360f, hudCam)
    val hudStg = Stage(hudView , hudSb)

    val dialogSb = SpriteBatch()
    val dialogCam = OrthographicCamera(width, height)
    val dialogView = StretchViewport(480f, 360f, dialogCam)
    val dialogStg = Stage(dialogView , dialogSb)

    val ic = InputCtl()
    val gpc = GamepadCtl()
    val osc = OnScreenGamepad()

    val sr = ShapeRenderer()

    val dialogSnd = Gdx.audio.newSound(Gdx.files.internal("dialog.ogg"))

    val skin = Skin(Gdx.files.internal("custom/skin/json-export.json"))

    lateinit var app: App

    init {
        skin.getFont("carbontype").data.setScale(0.5f)
        Scene2DSkin.defaultSkin = this.skin
    }
}