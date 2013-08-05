var arraySource = [];	//存储起点
var arrayTarget = [];	//存储重点Id

function putEdage(sourceId, targetId) {
	arraySource.push(sourceId);
	arrayTarget.push(targetId);
}

var divIdArray = [];		//存储画布的Id
function putCanvas(divId) {
	divIdArray.push(divId);
}

function drawChat() {
	jsPlumb.Defaults.Container = $("#" + divIdArray[0]);
	jsPlumb.setMouseEventsEnabled(true);
	
	jsPlumb.bind("ready", function() {
		document.onselectstart = function() {
			return false;
		};
	});
	
	var ff = {
			connector : "Bezier",
			anchors : ["AutoDefault", "AutoDefault"],
			paintStyle : {
				lineWidth : 13,
				strokeStyle : "#a7b04b",
				outlineWidth : 1,
				outlineColor : "#666"
			},
			endpointStyle : {
				fillStyle : "#a7b04b"
			},
			hoverPaintStyle : {
				lineWidth : 13,
				strokeStyle : "#7ec3d9"
			},
			overlays : [["Label", {
				cssClass : "l1 component label ",
				label : "",
				location : 0.5,
				events : {
					"click" : function(label, evt) {
						alert("clicked on label for connection 1");
					}
				}
			}], ["Arrow", {
						location : 0.2,
						width : 50,
						events : {
							"click" : function(arrow, evt) {
								alert("clicked on arrow for connection 1");
							}
						}
					}]]
		};
	
	jsPlumb.draggable(jsPlumb.getSelector(".itemWindow"));
	
    var width = $('#' + divIdArray[0]).width();
    var height = $('#' + divIdArray[0]).height();
    var g = new Graph();
    g.edgeFactory.template.style.directed = true;

	for(i=0; i<arraySource.length; i++) {
		g.addEdge(arrayTarget[i], arraySource[i]);
	}

    var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
    var renderer = new Graph.Renderer.Raphael(divIdArray[0], g, width, height, jsPlumb);	
}
