/**
 * 
 */

function postProcessExpand(sender, tree) {
	var node = getNode(sender, tree, "expandNode")
	// console.log(name(node) + " Expand Postprocessing: selected = " + isSelected(node) + ", expanded = " + isExpandeded(node));

	select(node);
	collapseSiblings(node)

	console.log("Postprocessing Complete");
}

function postProcessSelect(sender, tree) {
	var node = getNode(sender, tree, "selection");
	// console.log(name(node) + " Select Postprocessing: selected = " + isSelected(node) + ", expanded = " + isExpandeded(node));

	expand(node);
	collapseSiblings(node);

	console.log("Postprocessing Complete");
}

function collapseSiblings(node) {
	var siblings = node.parent().parent().parent().siblings();
	siblings.find(".ui-icon-minus").trigger("click");
}			

function getNode(sender, tree, eventName) {
	var textToSearch = decodeURIComponent(sender.data);
	var target = sender.source + "_" + eventName;
	var results = new RegExp('[\?&]' + target + '=([^&#]*)').exec(textToSearch);
	if (results === null) return null;

	var dataRowKey = results[1] || 0;
	var selector = "td[data-rowkey='" + dataRowKey + "']";
	return $(tree.jq).find(selector);
}			

function isSelected(node) {
	return node[0].classList.contains("ui-treenode-selected")
}

function select(node) {
	//if (!isSelected(node)) {
	node.find(".ui-tree-selectable").trigger("click");
	//}
}

function isExpandeded(node) {
	return !node[0].classList.contains("ui-treenode-collapsed")
}

function expand(node) {
	//if (!isExpandeded(node)) {
	node.find(".ui-icon-plus").trigger("click");
	//}
}

function name(node) {
	return node[0].innerText;
}

function quantityWarning(sender, dialog) {
	var quantity = parseInt(sender.value);
	console.log("Quantity = " + quantity);
	if (quantity > 1) { dialog.show(); }
}
