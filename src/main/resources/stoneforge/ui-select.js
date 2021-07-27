/*
 * Copyright (C) 2021 Mimic Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
import {Mimic as $} from  "./mimic.js"

export class Select extends $ {

	/** The assosiated model. */
	model = []

	/** The selected model */
	selected = new Set()

	/** The label builder for each model' */
	label = item => item.toString()

	/** The label builder for the selected values. */
	selectionLabel = selected => selected.map(this.label).join(", ")

	/** The flag to enable multiple selection. */
	multiple

	/** The default message */
	placeholder = "Select Item"

	/**
	 * Initialize by user configuration.
	 */
	constructor(config) {
		super("<o-select/>")
		Object.assign(this, config)

		this.set({ disabled: !this.model.length })
			.make("view").click(e => this.find("ol").has("active") ? this.close() : this.open())
			.make("now").text(this.placeholder).parent()
			.svg("/main.svg#chevron").parent().parent()
			.svg("/main.svg#x").click(e => this.deselect())
		this
			.make("ol").click(e => this.select(e.target.model, $(e.target)), { where: "li" })
			.make("li", this.model, (item, li) => li.text(this.label(item)))

		this.closer = e => {
			if (!this.nodes[0].contains(e.target)) this.close()
		}
	}

	/**
	 * Initialize by user configuration.
	 */
	select(item, dom) {
		if (this.multiple) {
			dom.toggle("select", () => this.selected.add(item), () => this.selected.delete(item))
		} else {
			if (this.selected.has(item)) {
				this.close()
				return
			}
			this.deselect(true)
			dom.add("select")
			this.selected.add(item)
		}
		this.update()
	}

	/**
	 * Initialize by user configuration.
	 */
	deselect(skipUpdate) {
		this.find("li").remove("select")
		this.selected.clear()
		this.close()

		if (!skipUpdate) this.update()
	}

	/**
	 * Initialize by user configuration.
	 */
	update() {
		this.find("now").set({ select: this.selected.size }).text(this.selectionLabel([...this.selected.keys()]) || this.placeholder)
		this.find(".x").set({ active: this.selected.size })
		this.dispatch("change")
	}

	/**
	 * Initialize by user configuration.
	 */
	open() {
		this.find("ol, .chevron").add("active")
		$(document).click(this.closer)
	}

	/**
	 * Initialize by user configuration.
	 */
	close() {
		this.find("ol, .chevron").remove("active")
		$(document).off("click", this.closer)
	}
}