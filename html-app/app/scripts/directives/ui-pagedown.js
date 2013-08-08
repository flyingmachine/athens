/*
 * from https://raw.github.com/programmieraffe/angular-editors/master/editors/pagedown-bootstrap/directive.js
 * usage: <textarea ui:pagedown ng:model="box.content"></textarea>
 */
angular.module('athensApp').directive('uiPagedown', function($compile) {
  var nextId = 0;
  
  //Make converter only once to save a bit of load each time - thanks to ajoslin
  var markdownConverter = new Markdown.Converter(); 
  
  return {
    require: 'ngModel',
    replace:true,
    template:'<div class="pagedown-editor"></div>',
    link:function (scope, iElement, iAttrs, ngModel) {
      var editorUniqueId = nextId++;
      
      var newElement = $compile('<div>'+
                                '<div class="wmd-panel">'+
                                '<div id="wmd-button-bar-'+editorUniqueId+'"></div>'+
                                '<textarea class="wmd-input" id="wmd-input-'+editorUniqueId+'">'+
                                '</textarea>'+
                                '</div>'+
                                '<div id="wmd-preview-'+editorUniqueId+'" class="wmd-panel wmd-preview"></div>'+
                                '</div>')(scope);
      
      iElement.html(newElement.html());
      
      var converter = new Markdown.Converter();
      
      var editor = new Markdown.Editor(converter, "-"+editorUniqueId, {
        strings: {
          imagedialog: "<p><strong>Insert Image</strong></p><p>http://example.com/images/diagram.jpg \"optional title\"</p>"
        }
      });
      
      editor.run();
      
      var $wmdInput = $("#wmd-input-"+editorUniqueId);
      
      
      // local -> parent scope change (model)
      $wmdInput.on('change',function(){
        // console.log('wmdInput changed',$wmdInput.val());
        var rawContent = $wmdInput.val();
        scope.$apply(function(){
          ngModel.$setViewValue(rawContent);
        });
      });
      
      // parent scope -> local change
      scope.$watch(iAttrs.ngModel, function(value,oldValue) {
        // console.log('ngModel changed','old: ',oldValue,'new: ',value,editor,$wmdInput);
        // this does not really work, so we do it manually - what is the correct way?
        // scope.textareaValue = value;
        if(value!==undefined){
          /*scope.$apply(function(){
            textareaValue = value;
            editor.refreshPreview();
            })*/
          $wmdInput.val(value);              
          //console.log($wmdInput.html());
          editor.refreshPreview(); // forces the editor to re-render the preview according to the contents of the input, e.g. after you have programmatically changed the input box content. This method is only available after editor.run() has been called.
        }
        
      });            
    }
  }
});
