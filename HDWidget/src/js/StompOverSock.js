
var StompOverSock = (function(){
	var stompProcessor;
	return {
		getInstance:function(isDdebug){
			if(!stompProcessor)
				stompProcessor = initStompOverSock(isDdebug);
			return stompProcessor;
		}
	}
})();


function initStompOverSock(debugParam){
	var debug = debugParam;
	if(debug)console.log("################ initStompOverSock method ! ###############");	
	var endPoint = "http://localhost:8080/HDServer/wss";//var endPoint = "http://10.56.2.228:9997/wss";
	var socket = new SockJS(endPoint);	
	var stompClient = webstomp.over(socket);
	//var wsoc = new WebSocket(endPoint);
	//var stompClient = webstomp.over(wsoc);
	//var stompClient = webstomp.client(endPoint);
	if(!debug)
		stompClient.debug = () => {};// For disable stomp logging	
	var unsuccessSubscribeList = [];
	var unsuccessSendList = [];
	var connected = false;	
	
	var connectHandler = function(asa){
		if(debug){
			console.log("################ Connect Handler method ! ###############");
			console.log("socket.httpCode AT connectHandler: ", SockJS.httpCode);		
			console.log("asa", asa);
//			var unsuccessSubscribeList1 = unsuccessSubscribeList;
//			var unsuccessSendList1 = unsuccessSendList;
//			console.log("11111111  unsuccessSubscribeList1: ", unsuccessSubscribeList1);
//			console.log("11111111  unsuccessSendList1: ", unsuccessSendList1);
		}			
		connected = true;	
		if(unsuccessSubscribeList.length>0){
			unsuccessSubscribeList = unsuccessSubscribeList.removeDublicateObject("path");
//			var unsuccessSubscribeList2 = unsuccessSubscribeList; 
//			console.log("222222222222   unsuccessSubscribeList2: ", unsuccessSubscribeList2);
			unsuccessSubscribeList.forEach(function(elt, i, array) {
				stompHolder.subscribe(elt.path, elt.callback);
			})
		}		
		if(unsuccessSendList.length>0){
			unsuccessSendList = unsuccessSendList.removeDublicateObject("path");
//			var unsuccessSendList2 = unsuccessSendList; 
//			console.log("222222222222  unsuccessSendList2: ", unsuccessSendList2);
			unsuccessSendList.forEach(function(elt, i, array) {
				stompHolder.send(elt.path, elt.body, elt.headers);
			})				
		}
		if(debug){
			console.log("unsuccessSubscribeList: ", unsuccessSubscribeList);
			console.log("unsuccessSendList: ", unsuccessSendList);
		}
	}
	
	var connectErrorHandler = function(errorMessage){
		if(debug){
			console.log("################ Error Handler method ! ###############");		
			console.log("socket.httpCode AT ErrorHandler: ", SockJS.httpCode);			
		}
		if(SockJS.httpCode==401){
			window.location='/';
			return;
		}				
		 connected = false;
		 socket = new SockJS(endPoint);
		 stompClient = webstomp.over(socket);
		 //wsoc = new WebSocket(endPoint);		 
		 //stompClient = webstomp.over(wsoc);
		 //stompClient = webstomp.client(endPoint);
		 if(!debug)
				stompClient.debug = () => {};// For disable stomp logging
		 stompClient.connect({}, (mes)=>{connectHandler(mes);}, (erorMes)=>{setTimeout(()=>{connectErrorHandler(erorMes);}, 3000); });		 
	}
	
	
	var stompHolder = {
			subscribeList:[],											
			
			subscribe:function(path, callbackFunction){
				var subscribeId;
				if(connected && this.subscribeList.indexOf(path)===-1){					
					subscribeId = stompClient.subscribe(path, callbackFunction);
					this.subscribeList.push({path:path, id:subscribeId, callback:callbackFunction});
				}
				else
					unsuccessSubscribeList.push({path:path, id:null, callback:callbackFunction});
			},
			
			send:function(path, body, headers){
				if(connected){
					if(debug)
						console.log("################ SEND to :", path, "   body: ", body,"   headers: ", headers);
					stompClient.send(path, body, headers);
				}
				else
					unsuccessSendList.push({path:path, body:body, headers:headers});
			},
			
			disconnect: function(callbackFunction, headers){
				stompClient.disconnect(callbackFunction, headers);
			}						
	};
	
	stompClient.connect({}, function(mes){connectHandler(mes);}, function(erorMes){connectErrorHandler(erorMes);});	
	
	return stompHolder;
}