const dev = /*[[${dev}]]*/ false;
const id = 'symphony-tabs' + (dev ? '-local' : '')
var viewName = id+ ':view'
var baseUrl = /*[[${baseUrl}]]*/ 'https://localhost:4000';
var jwt;

SYMPHONY.remote.hello().then(function (data) {

	SYMPHONY.application
		.connect('symphony-tabs', ['symphony-tabs:controller', 'dialogs', 'share', 'modules', 'extended-user-info'], [viewName])
		.then(function (response) {
//			var modulesService = SYMPHONY.services.subscribe('modules')
//			var shareService = SYMPHONY.services.subscribe('share');
			var extendedUserInfo = SYMPHONY.services.subscribe('extended-user-info');

			extendedUserInfo.getJwt().then(function (t) {
				jwt = t;
			});
		})
		.catch(e => {
			console.log("couldn't get jwt");
		});

	var tableData = [
	 	{id:1, ISIN:"Test Bond", CCY:"USD", Amount: 1000000},
	 	{id:2, ISIN:"Another Test Bond", CCY:"GBP", Amount: 500000}
	 ];
	
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
				{ title: "Amount", field: "Amount", sorter: "number", align:"right", formatter:"money", formatterParams:{precision:0}, editor:true} 
			],
			renderComplete: function(x) {
				console.log('done');
			},
			downloadReady: function(fileContents, blob) {
				
				var json = { rows: JSON.parse(fileContents) };
				
				console.log(json);

				postData(baseUrl+'/table/nhi8bALLVji6w33tZ3iuEX___pLaltUjdA', json)
					.then(data => console.log(JSON.stringify(data)))
					.catch(error => console.error(error));	
				
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
	
	function postData(url = '', data = {}) {
		  // Default options are marked with *
		    return fetch(url, {
		        method: 'POST', // *GET, POST, PUT, DELETE, etc.
		        mode: 'cors', // no-cors, cors, *same-origin
		        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
		        credentials: 'same-origin', // include, *same-origin, omit
		        headers: {
		        	'Authorization': 'Bearer '+jwt,
		            'Content-Type': 'application/json',
		            // 'Content-Type': 'application/x-www-form-urlencoded',
		        },
		        redirect: 'follow', // manual, *follow, error
		        referrer: 'no-referrer', // no-referrer, *client
		        body: JSON.stringify(data), // body data type must match "Content-Type" header
		    })
		    .then(response => response.json()); // parses JSON response into native JavaScript objects 
	}		
		
})