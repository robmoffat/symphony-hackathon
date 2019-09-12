const id = 'symphony-tabs'
const outServiceName = id +':controller';
const outService = SYMPHONY.services.register(outServiceName)
const appName = 'Symphony Tabs';
const inServices = ['modules', 'applications-nav', 'ui', 'share', 'extended-user-info', 'entity'];
const baseUrl = /*[[${baseUrl}]]*/ 'https://localhost:4000';
var inModules = {};
var tokens = {};

SYMPHONY.remote.hello()
.then(function (data) {
	// get pod token here
	return fetch('[(${podAuthUrl})]'+data.pod);
})
.then(function (response) {
	return response.json();
})
.then(function (appData) {
	tokens['appToken'] = appData.tokenA;
	if (appData.dev) {
		return SYMPHONY.application.register(id, inServices, [ outServiceName ]);
	} else {
		return SYMPHONY.application.register(appData, inServices, [outServiceName]);
	}
})
.then(function (response) {
	tokens['podToken'] = response.tokenS;
	return fetch('[(${appAuthUrl})]'+'appToken='+encodeURI(tokens['appToken'])+'&podToken='+encodeURI(tokens['podToken']));
})
.then(function (response) {
   inServices.forEach(name => inModules[name] = SYMPHONY.services.subscribe(name));
   const modulesService = inModules['modules'];
   const navService = inModules['applications-nav'];
   const entityService = inModules["entity"];
   const uiservice = inModules['ui'];
    
   entityService.registerRenderer('com.db.symphonyp.tabs', {}, outServiceName)

   navService.add(id+"-nav", appName, outService.name);
   
   uiservice.registerExtension(
	"single-user-im",
	"hello-im",
	outServiceName,
	{
		label: "Create table",
		data: {"datetime": Date()}
	}
   );

   outService.implement({
     select: function(e) {
       if (e == id+'-nav') {
         modulesService.show(
          id+"-app-panel", 
          {title: 'Some Module'}, 
          outService.name, 
          baseUrl+"/app.html",
          {canFloat: true});
       }
     },
     trigger: function(uiClass, id, payload, data) {
    	 console.log("Create table button pressed");
     }
   })

   console.log(appName+" controller initialized");
})
.catch(function(err) {
   console.error(err);
   console.error("Couldn't complete registration: "+appName);
})
