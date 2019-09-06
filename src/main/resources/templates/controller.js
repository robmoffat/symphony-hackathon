var controllerId = 'multimeter:controller'
var multimeterService = SYMPHONY.services.register(controllerId)

SYMPHONY.remote.hello().then(function (data) {
  SYMPHONY.application
    .register('multimeter', [ 'entity', 'modules', 'applications-nav' ], [ controllerId ])
    .then(function (response) {
      var modulesService = SYMPHONY.services.subscribe('modules')
      var navService = SYMPHONY.services.subscribe('applications-nav')
      var baseUrl = /*[[${baseUrl}]]*/ 'https://localhost:4000';

      navService.add('multimeter-nav', 'Multimeter - HAR Analysis', controllerId)

      multimeterService.implement({
        select: function (id) {
          if (id === 'multimeter-nav') {
            modulesService.show(
              'multimeter:page',
              { title: 'Symphony Multimeter - HAR Analysis' },
              controllerId,
              baseUrl+'/har.html',
              { 'canFloat': true }
            )
          }
        }
      })
    })
})
