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


		var tableData = [
		 	{id:1, row:"Test Bond"},
		 	{id:2, row:"Another Test Bond"}
		 ];

		window.addEventListener("load", function() {
			console.log("loaded");
			var table = new Tabulator("#table", {
				data: tableData,
				height: "500px",
				columns: [
					{ title: "Bond", field: "row", sorter: "number", width: 80}, 
				],
				renderComplete: function(x) {
					console.log('done');
				}
			});
		},false);

//		});
//})
