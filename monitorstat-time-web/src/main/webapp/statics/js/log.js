// common ����־
var log = getLogger();
function getLogger() {
   // �Ƿ������־
   var debug = true;
   if (!debug){
      return function() {
      };
   }

   if (typeof console != 'undefined')
      return console.log;
      

   return alert;
}