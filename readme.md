# YarnGdx Experiment

[YarnGdx](https://github.com/kyperbelt/YarnGdx) is an interpreter for the [yarn language](https://github.com/InfiniteAmmoInc/Yarn), which is used for creating dialog trees. What you get with Yarn is an editor for creating 'nodes', which contain little scripts. The scripts can contain lines of dialog and special directives that can be used for simple if/else logic, jumping to other nodes, or changing the game state through custom 'functions' or 'commands'.

![dialog.gif](.github/dialog.gif?raw=true)

What this does for you is provide you with a visual scripting medium to create your dialogs. It also provides a way to separate the script content from your game logic (you have the choice between json and yarn.txt files).

In your game, you will have to monitor the current state of the dialog tree as the variables below change as you step through the dialog - 

- currentLine: Is there a line of dialog?
- currentOptions: Are there options the user needs to choose from to progress to the next step?
- nodeComplete: Has the node ended? (a dialog can consist of multiple nodes, so the node being complete doesn't mean the dialog is finished)
- currentCommand: Is there a custom directive?

This is great way to leverage a simple dsl or scripting language to build a narrative structure in your game. One could compare it to the 'scummvm' used for the classic lucasarts games. When used correctly it can speed up your development significantly and change your approach to game design.

~~Unfortunately, there's a known bug which prevents the parser from loading scripts on android (has something to do with regular expressions on java 1.6). Functionality is limited to having a single node without any branching, conditionals, changing nodes, or custom directives. Basically, no directives will work at all on android until the library is fixed.~~

The issue is fixed in this repo's (yarngdx dependency)[https://github.com/jojomickymack/yarnjava]. I reported the problem [here](https://github.com/kyperbelt/YarnGdx/issues/2).

# Using Jitpack Dependency For Yarngdx

In order to avoid including the source code for yarngdx, I am pulling it in as a dependency using [jitpack](https://jitpack.io/#jojomickymack/yarnjava/v1.0). This makes your github repo act like a dependency from [jcenter](https://bintray.com/bintray/jcenter). It's perfect for distributing small libraries like this.

note: With yarn, you have the option of exporting to json or yarn.txt files - both can be loaded by yarngdx, but yarn.txt is a lot more human readable - it looks a lot like yaml.

Watch out for windows CR+LF line endings if you are manipulating yarn.txt files on windows - saving with CR+LF will make it so yarngdx won't be able to read the files - to avoid issues, save yarn.txt files with LF/unix style line endings. I use [notepad2](http://www.flos-freeware.ch/notepad2.html) and there's a line ending option to quickly convert CR+LF to LF.

