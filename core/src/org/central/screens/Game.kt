package org.central.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList
import com.badlogic.gdx.scenes.scene2d.ui.Window
import org.central.App
import org.central.AppObj
import org.central.dialog.TypewriterAct
import com.kyper.yarn.Dialogue
import com.kyper.yarn.DialogueData
import ktx.actors.plusAssign
import ktx.app.KtxScreen
import org.central.dialog.DialogBox
import org.central.dialog.DialogState


class Game(val application: App) : KtxScreen {

	private var variables = DialogueData("")
    private var dialog = Dialogue(variables)
    private val ds = DialogState(dialog)

    private val dialogBox = DialogBox()
    private val responseBox = Window("", AppObj.skin, "black-bg")

    private var responseList = GdxList<String>(AppObj.skin)

    private var lastLine = ""
    private var lastResponseList = GdxList<String>(AppObj.skin)

    init {
        Gdx.input.inputProcessor = AppObj.hudStg

        /**
         * variables used here are has_diamond and has_shovel - in the dialog itself, the state of
         * the variable can be changed with a directive, i.e.
         * <<set $has_shovel to true>>
         */
        variables.put("\$has_diamond", false)
        variables.put("\$has_shovel", false)

        /**
         * library.registerFunction is a way to make it so you can call a function directly from your script, i.e.
         * <<setDamselAction("talk")>>
         *
         * The other way to achieve almost the same thing is with 'commands'. The difference is minor - with commands, you're not
         * restricted to a method-like syntax with parenthesis and can use a more english like expression - 'setSprite GravediggerExpression happy'
         * might be a good example
         *
         * what's nice about using functions instead from my view, is that you don't have the logic with the script-parsing code
         * if you are going to re-use your script logic for many different dialogs, you'd have to have the command logic for all of
         * them in the same place.
         *
         * it comes down to preference
         */
        dialog.library.registerFunction("setDamselAnimation", 1) { params ->
            val action = params[0]
            GameObj.damsel.currentAnimation = if (action.stringValue in GameObj.damsel.animations) GameObj.damsel.animations[action.stringValue]!!
            else GameObj.damsel.animations["static_normal"]!!
        }

        dialog.library.registerFunction("selectCharacter", 0) {
            GameObj.selectCharacter = true
        }

        /**
         * dialog files are 'stacked' into the same Dialogue object - really they are just collections of 'nodes'
         */
        dialog.loadFile("gravedigger.yarn.txt", false, false, null)
        dialog.loadFile("damsel.yarn.txt", false, false, null)

        /**
         * these game characters are 'Actors' and can be added to the stage so their act() and draw() methods are called
         * when the stage's act() and draw() methods are called in the game's render method
         */
        AppObj.stg += GameObj.gravedigger
        AppObj.stg += GameObj.damsel

        /**
         * the boxes where the text appears are also actors
         */
        AppObj.dialogStg += dialogBox
        AppObj.dialogStg += responseBox

        /**
         * the on screen keyboard for mobile - this is probably unnecessary for desktop targets
         */
        AppObj.hudStg += AppObj.osc

        dialogBox.setDialogSize(AppObj.cam.viewportWidth, 120f, 0f, 120f)

        /**
         * the dialogBox is something I reused from somewhere else - the responseBox here isn't so I manually
         * set the same position and padding (same code is inside of dialogBox)
         */
        responseBox.setSize(AppObj.cam.viewportWidth - 16f * 2, 120 - 16f * 2)
        responseBox.setPosition(0 + 16f, 16f + 36f)

        responseBox.add(responseList).size(responseBox.width - 2 * 20f, responseBox.height - 2 * 20f)
	}

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        updateInput()
        ds.processDialogState()
        val texts = ds.getTexts()

        /**
         * this lastLine variable is a simple system to make it so the text only gets set once when it changes
         */
        if (texts.line != lastLine) {
            /**
             * this TypewriterAct is a custom action to make the text progressively appear
             */
            dialogBox.dialogLabel.addAction(TypewriterAct(texts.line))
            lastLine = texts.line
        }

        /**
         * the responseBox is unique from the dialogBox because the text appears immediately and because you can select a response
         */
        if (texts.responseList.items != lastResponseList.items) {
            texts.responseList.items.forEach{println(it)}
            responseList.setItems(texts.responseList.items)
            lastResponseList.setItems(texts.responseList.items)
        }

        AppObj.stg.act(delta)
        AppObj.stg.draw()

        AppObj.dialogStg.act(delta)
        AppObj.dialogStg.draw()

        AppObj.hudStg.act(delta)
        AppObj.hudStg.draw()
	}

    /**
     * this is pretty messy, note the repeated code for selecting a different character to talk to/ response option
     */
    fun updateInput() {
        if (GameObj.selectCharacter) {
            if (AppObj.ic.aPressed) {
                if (responseList.selectedIndex == 0) {
                    dialog.start("Gravedigger")
                    GameObj.selectCharacter = false
                    AppObj.ic.aPressed = false
                    ds.resetAllResults()
                }

                if (responseList.selectedIndex == 1) {
                    dialog.start("Damsel")
                    GameObj.selectCharacter = false
                    AppObj.ic.aPressed = false
                    ds.resetAllResults()
                }
            }

            if (AppObj.ic.rPressed) {
                AppObj.ic.rPressed = false
                if (responseList.selectedIndex + 1 >= responseList.items.size) responseList.selectedIndex = 0
                else responseList.selectedIndex++
            }

            if (AppObj.ic.lPressed) {
                AppObj.ic.lPressed = false
                if (responseList.selectedIndex - 1 < 0) responseList.selectedIndex = responseList.items.size - 1
                else responseList.selectedIndex--
            }
        } else {
            if (ds.currentLine != null && ds.currentOptions == null) {
                if (AppObj.ic.aPressed) {
                    AppObj.ic.aPressed = false
                    ds.currentLine = null
                }
            } else if (ds.currentOptions != null && responseList.items.size > 0) {
                if (AppObj.ic.aPressed) {
                    AppObj.ic.aPressed = false
                    ds.currentOptions!!.choose(responseList.selectedIndex)
                    ds.currentOptions = null
                    ds.currentLine = null
                }

                if (AppObj.ic.rPressed) {
                    AppObj.ic.rPressed = false
                    if (responseList.selectedIndex + 1 >= responseList.items.size) responseList.selectedIndex = 0
                    else responseList.selectedIndex++
                }

                if (AppObj.ic.lPressed) {
                    AppObj.ic.lPressed = false
                    if (responseList.selectedIndex - 1 < 0) responseList.selectedIndex = responseList.items.size - 1
                    else responseList.selectedIndex--
                }
            }
        }
    }
}
