var viewName = 'symphony-tabs:view'
var baseUrl = /*[[${baseUrl}]]*/ 'https://localhost:4000';

//SYMPHONY.remote.hello().then(function (data) {
//
//	SYMPHONY.application
//		.connect('symphony-tabs', ['symphony-tabs:controller', 'dialogs', 'share', 'modules'], [viewName])
//		.then(function (response) {
//			var multimeter = SYMPHONY.services.subscribe('symphony-tabs:controller')
//			var modulesService = SYMPHONY.services.subscribe('modules')
//			var shareService = SYMPHONY.services.subscribe('share');

			var table = new Tabulator("#table", {
				data: tableData,
				height: "100%",
				columns: [
					{ title: "Bond", field: "row", sorter: "number", width: 80}, 
				],
				renderComplete: function(x) {
					console.log('done')
				},
			});
//		});
//})
