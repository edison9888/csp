// common 简单日志
var log = getLogger();
function getLogger() {
   // 是否输出日志
   var debug = true;
   if (!debug){
      return function() {
      };
   }

   if (typeof console != 'undefined')
      return console.log;
      

   return alert;
}