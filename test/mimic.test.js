import { Mimic as $ } from '../docs/mimic.js';
import { assert } from "@esm-bundle/chai"

it("Rin tried:", () => {
	assert.equal($("<div>"), "");
});
