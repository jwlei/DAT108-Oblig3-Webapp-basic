"use strict";

class DiceController {

    constructor(elementId) {
        this.run = this.run.bind(this);
        this.rollDice = this.rollDice.bind(this);

        this.root = document.getElementById(elementId);
        this.button = this.root.querySelector('*[data-dicebutton]')
        this.output = this.root.querySelector('*[data-diceoutput]');
        this.dice = new Dice();
    }

    run() {
        this.button.addEventListener('click', this.rollDice);
    }

    rollDice() {
        this.output.innerText = this.dice.roll();
    }
}



//Dice 
class Dice {
		constructor(value) 
			{
			this.value = value;
			}

		roll() {
			return (this.value = 1 + Math.floor(Math.random() * 6));
				}
}

const controller = new DiceController("root");

document.addEventListener("DOMContentLoaded", controller.run, true);