var multimeterName = 'multimeter:view'

SYMPHONY.remote.hello().then(function (data) {
  var themeColor = data.themeV2.name
  var themeSize = data.themeV2.size
  document.body.className = 'symphony-external-app ' + themeColor + ' ' + themeSize

  SYMPHONY.application
    .connect('multimeter', ['multimeter:controller', 'dialogs'], [multimeterName])
    .then(function (response) {
      var multimeter = SYMPHONY.services.subscribe('multimeter:controller')
      var dialogService = SYMPHONY.services.subscribe('dialogs')

      
      // wire up record / share / save buttons.
    
    })
})
