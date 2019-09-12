const dev = /*[[${dev}]]*/ false;
const id = 'symphony-tabs' + (dev ? '-local' : '')
var viewName = id+ ':view'
var baseUrl = /*[[${baseUrl}]]*/ 'https://localhost:4000';

//SYMPHONY.remote.hello().then(function (data) {
//
//	SYMPHONY.application
//		.connect('symphony-tabs', ['symphony-tabs:controller', 'dialogs', 'share', 'modules'], [viewName])
//		.then(function (response) {
//			var multimeter = SYMPHONY.services.subscribe('symphony-tabs:controller')
//			var modulesService = SYMPHONY.services.subscribe('modules')
//			var shareService = SYMPHONY.services.subscribe('share');

		if (table == null) {
			var tableData = [
			 	{id:1, ISIN:"Test Bond", CCY:"USD", Amount: 1000000},
			 	{id:2, ISIN:"Another Test Bond", CCY:"GBP", Amount: 500000}
			 ];
		} else {
			tableData = table.rows;
		}
		
		var table;
		
		var currentRow;

		window.addEventListener("load", function() {
			console.log("loaded");
			table = new Tabulator("#table", {
				addRowPos:"bottom",
				layout:"fitDataFill",
				data: tableData,
				height: "500px",
				selectable:true,
				columns: [
					{ title: "ISIN", field: "ISIN", sorter: "string", editor:true}, 
					{ title: "CCY", field: "CCY", sorter: "string", editor:true}, 
					{ title: "Amount", field: "Amount", sorter: "number", editor:true} 
				],
				renderComplete: function(x) {
					console.log('done');
				},
				downloadReady: function(fileContents, blob) {
					console.log(fileContents);
					return false;
				}
			});
			
		},false);

		function addRow() {
			table.addRow();
//			table.addRow({id: 0, ISIN:"Added Row", CCY:"EUR", Amount:0});
		}
		
		function deleteRow() {
			table.getSelectedRows().forEach(function(row) {
				console.log(row.getData());
				row.delete();
			});
		}
		
		function send() {
			table.download("json", "data.json")
		}
		
//		});
//})
