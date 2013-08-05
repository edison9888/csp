function createNode(){
  var root = {
    "id" : "0",
    "text" : "root",
    "value" : "86",
    "showcheck" : true,
    complete : true,
    "isexpand" : true,
    "checkstate" : 0,
    "hasChildren" : true
  };
  var arr = [];
  for(var i= 1;i<2; i++){
    arr.push( {
      "id" : "node-" + i,
      "text" : "node-" + i,
      "value" : "node-" + i,
      "showcheck" : true,
      complete : true,
      "isexpand" : false,
      "checkstate" : 0,
      "hasChildren" : false
    });
  }
  root["ChildNodes"] = arr;
  return root; 
}

treedata = [createNode()];
