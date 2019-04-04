function survey(){
	document.getElementById("que1").innerHTML = "Q1:How Would you Rate yourself";
	document.getElementById("question2").style.display = "none";
	document.getElementById("question3").style.display = "none";
	document.getElementById("question4").style.display = "none";
	// document.getElementById("radioText").innerHTML = "2000";//To be used for changing the contents 
}
var response1;
function SubmitForm(PrepareString){
	var radios = document.getElementsByName(PrepareString);
	for (var i = 0, length = radios.length; i < length; i++) {
		if (radios[i].checked) {
			document.getElementById("question1").style.display = "none";
			document.getElementById("question2").style.display = "block";
			callPostMethod(PrepareString,radios[i].value);
			document.getElementById("que2").innerHTML = response1;
			break;
		}
	}
}


function callPostMethod(ResponseToBeSent,TheSelectedValue) {
	// debugger;


	$.ajax({	
              url: 'InitialAppHandler', //this will use the form's action attribute
              type: 'post',
              data: { "method" : "FETCH_ENROLL_QUES",  "rating" : "1" },
              contentType: "application/json",
              success: function(responseData){
              var response = CaptureResponse(responseData); //Function For responseData
                 alert("This is the response" +response);
              }
	});
	// debugger;
};

function CaptureResponse(ReturnResponse){
             var obj = JSON.stringify(ReturnResponse);
             var data = JSON.parse(obj);
             return data;
	 }
		

/*
function CaptureResponse(ReturnResponse){
          if(ReturnResponse.QuestionNO == 1)//
          {
         Substitute(ReturnResponse.Question);
             //This function will substitute the Response with new content
          }
    }

*/

/*	function Substitute(QuestionContent){
			var element, newElement, parent;

			// Get the original element
			element = document.getElementById("Question");
			
			// Assuming it exists...
			if (element) {
				// Get its parent
				parent = element.parentNode;
			
				// Create the new element
				newElement = document.createElement('div');
			
				// Set its ID and content
				newElement.id = "Question";
				newElement.innerHTML = QuestionContent.Question;
				parent.insertBefore(newElement, element);
				parent.removeChild(element);
			}
	}
*/

/*function CallPostMethod(){
	debugger;
	var hr = new XMLHttpRequest();
   var url = "https://gurujsonrpc.appspot.com/guru";//Host Url  
   hr.open("POST", url, true);
   hr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
   hr.onreadystatechange = function(){
	 if (hr.readyState == 4 && hr.status == 200) {
		debugger;
	   var resp = console.log(response);
	   if (resp == "true") {
         debugger;
	 }
   }
   hr.send({ "method" : "guru.test", "params" : [ "Guru" ], "id" : 123 });
   }
}*/
