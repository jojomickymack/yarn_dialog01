package org.central.dialog

import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.utils.Array
import com.kyper.yarn.Dialogue
import org.central.AppObj
import org.central.screens.GameObj


class TextAreas(line: String, responseList: List<String>) {
    val line = line
    val responseList = responseList
}

class DialogState(initialDialog: Dialogue) {

    val dialog = initialDialog

    /**
     * the variables below represent the current state of the dialog tree as you step through it
     * calling nextAs... moves the dialog ahead
     */
    var currentLine: Dialogue.LineResult? = null
    var currentOptions: Dialogue.OptionResult? = null
    var currentCommand: Dialogue.CommandResult? = null
    var nodeComplete: Dialogue.NodeCompleteResult? = null

    /**
     * I separated processDialog from the rest because you're unlikely to need to change it
     * the getTexts and executeCommand methods are connected to the specifics of the game to some degree, but
     * processDialog is not
     *
     * note that 'nodeComplete' doesn't mean that the dialog (which can have many nodes) is finished
     * it means that a node is done
     */
    fun processDialogState() {
        if (AppObj.debug) println("""
processDialogState():
currentLine: $currentLine
currentOptions: $currentOptions
nodeComplete: $nodeComplete
currentCommand: $currentCommand""")

        if (currentLine == null && dialog.isNextLine) {
            currentLine = dialog.nextAsLine
            println("currentLine")
            println(currentLine!!.text)
        } else if (currentCommand == null && dialog.isNextCommand) {
            currentCommand = dialog.nextAsCommand
            executeCommand(currentCommand)
        } else if (currentOptions == null && dialog.isNextOptions) currentOptions = dialog.nextAsOptions
        else if (nodeComplete == null && dialog.isNextComplete) nodeComplete = dialog.nextAsComplete

        else if (currentLine == null && nodeComplete != null) {
            resetAllResults()
            dialog.stop()
        }
    }

    /**
     * if things aren't working right (lines getting skipped etc), you should enable debugging and compare the
     * state of our 4 variables when processDialog is called and when getTexts is called. Chances are, resetAllResults
     * was called when you didn't think think it was
     */
    fun resetAllResults() {
        if (AppObj.debug) println("now resetting")
        currentLine = null
        currentOptions = null
        nodeComplete = null
        currentCommand = null
    }

    /**
     * obviously your game might have a completely different system of interpreting the current state of the dialog
     *
     * for this one, there's two text areas, the one on top contains what the character is saying, the one on the bottom
     * contains your response options - thus I'm using a custom object TextAreas to return those fields back to the game's
     * render loop
     */
    fun getTexts(): TextAreas {
        var line = ""
        var responseArray = Array<String>()
        var responseList = com.badlogic.gdx.scenes.scene2d.ui.List<String>(AppObj.skin)

        if (AppObj.debug) println("""
getTexts():
currentLine: $currentLine
currentOptions: $currentOptions
nodeComplete: $nodeComplete
currentCommand: $currentCommand""")

        if (currentLine != null) line = currentLine!!.text

        if (currentOptions != null) {
            currentOptions!!.options.forEachIndexed { i, o ->
                responseArray.add("[${i + 1}]: $o")
            }
        } else {
            if (!GameObj.selectCharacter) responseArray.add(GameObj.advanceText)
            else {
                responseArray = GameObj.initialChoice
            }
        }

        if (responseArray.size > 0) responseList.setItems(responseArray)
        return TextAreas(line, responseList)
    }

    /**
     * I have mixed feelings about the use of commands in yarn when compared to Dialog.library.registerFunction - mainly because
     * it requires some implementation specific functionality as shown below - if I wanted to use this same class over and over
     * for different dialogs, all the commands would have to be in here.
     *
     * The advantage of using executeCommand over registerFunction is that you can have a lot more variation in the commands
     * - here the command 'setSprite GravediggerExpression neutral' is interpreted, but since the params are unlimited, you could
     * have all sorts of directives that read like sentences.
     */
    private fun executeCommand(currentCommand: Dialogue.CommandResult?) {
        if (currentCommand != null) {
            println("executing command: ${currentCommand.command}")
            val pattern = "\\s+".toRegex()
            val params = pattern.split(currentCommand.command)

            if (params[0] == "setSprite") {
                if (params[1] == "GravediggerExpression") {
                    GameObj.gravedigger.currentTex = if (params[2] in GameObj.gravedigger.sheetTex) GameObj.gravedigger.sheetTex[params[2]]!!
                    else GameObj.gravedigger.sheetTex["neutral"]!!
                }
            }
            this.currentCommand = null
        }
    }
}