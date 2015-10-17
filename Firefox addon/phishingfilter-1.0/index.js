require("sdk/tabs").on("ready", logURL);


var domain_list = [".com",".net",".org",".uk",".in",".us"];
var search_engine = ["www.google.com","r.search.yahoo.com","www.bing.com","search.yahoo.com"];
var buttons = require('sdk/ui/button/action');



var page_title;
var page_url;
var button = buttons.ActionButton({
  id: "mozilla-link",
  label: "Visit Mozilla",
  icon: {
    "16": "./icon-16.png",
    "32": "./icon-32.png",
    "64": "./icon-64.png"
  },
});

const defaultState = {
  "label": "your protected!",
  "icon": "./ok.png",
}

const differentState = {
  "label": "Phishing Alert!!",
  "icon": "./not-ok.png",
}

const {Cc,Ci} = require("chrome")
const prefs = require("simple-prefs");

function logURL(tab) {  

  var parameter_1 = false;    // similarity betweeen title and url
  var parameter_2 = false;    // Presense of Port number and IP address in URL
  var parameter_3 = false;    // Secure connection
  var parameter_4 = false;    // multiple domains emebedded in hostname
  var parameter_5 = false;    // presence of obfuscation with unusual character.
  var parameter_6 = false;    // domains embedded in url except hostnmae

  var url = require("sdk/url").URL(tab.url);
  var pathname = url.pathname;
  var host = url.host;
  var port = url.port;
  page_url = tab.url;
  page_title = tab.title;
    
  
  console.log("websiteurl ::: "  + url );
  //console.log("")
  var isPhishing = true;
  
  var webapiurl = "http://localhost:8090/phishing/isPhishing?websiteUrl=" + url;
  var Request = require("sdk/request").Request;
  var latestTweetRequest = Request({
  url: webapiurl,
  onComplete: function (response) {
    var website = response.json;
    console.log("Isphishing: " + website.isPhishing);
    console.log("Protocol " + website.protocol);
    isPhishing = website.isPhishing;
    //string_name = website.singer;
    //console.log("String value is :: " + string_name);
    var response_para =   "protocol=" + website.protocol + "&" 
    					+ "frequentTermsvsRegistrant=" + website.frequentTermsvsRegistrant + "&"
    					+ "noofDomainNamecandidatesinHostnameandContent=" + website.noofDomainNamecandidatesinHostnameandContent + "&"
    					+ "noofDomainNamecandidatesinHostname=" + website.noofDomainNamecandidatesinHostname + "&"
    					+ "numericHostname=" + website.numericHostname + "&"
    					+ "titlevsRegistrant=" + website.titlevsRegistrant + "&"
    					+ "titlevsDomainNamecandidatesinHostname=" + website.titlevsDomainNamecandidatesinHostname + "&"
    					+ "frequentTermsvsHostDomain=" + website.frequentTermsvsHostDomain + "&"
    					+ "copyrightvsRegistrant=" + website.copyrightvsRegistrant + "&"
    					+ "domainNamecandidatesinAnchorsvsRegistrant=" + website.domainNamecandidatesinAnchorsvsRegistrant + "&";


    if (isPhishing) {
      var prompts = Cc["@mozilla.org/embedcomp/prompt-service;1"]
                          .getService(Ci.nsIPromptService);

      var check = {value: false};                  // default the checkbox to false

      var flags = prompts.BUTTON_POS_0 * prompts.BUTTON_TITLE_SAVE +
                  prompts.BUTTON_POS_1 * prompts.BUTTON_TITLE_CANCEL;
      // This value of flags will create 3 buttons. The first will be "Save", the
      // second will be the value of aButtonTitle1, and the third will be "Cancel"

      var button1 = prompts.confirmEx(null, "Phishing Attack Ahead!!!", "This Website might contain some unwanted code which may try to theft you confideintial data whihout of your knowledge. If you feel that this website is safe and secure to use then press \"Continue\" otherwise press \"Cancel\".",
                                     flags, "", "", "", null, check);
      console.log("Pressed Button is :" + button1);
      button.state(button, differentState);

      if (button1==1) {
        response_para += "isphishing=false";   
      }
      else {
      	response_para += "isphishing=true";
      }

      var webapiurl1 = "http://localhost:8090/phishing/isPhishing/update?websiteUrl=" + url + "&" + response_para;
      console.log("URL is :: " + webapiurl1);
      var latestTweetRequest1 = Request({
		  url: webapiurl1,
		  onComplete: function (response) {
		    var tweet = response.json;
		    console.log("Done " + tweet.title );
		    console.log("Tweet: " + tweet.singer);
		  }
	  }).post();

    }
    else{
      button.state(button, defaultState);
    }

  }
  }).get();


}

function is_search_engine(page_url) {
  var search_engine_find = false;
  for (var i=0;i<search_engine.length;i++) {
    if (search_engine[i]==page_url) {
      
      search_engine_find = true;
      break;
    }
  }
  return search_engine_find;
}

function find_similarity(array_titles) {
  var match_no = 0;
  for (var i=0;i<array_titles.length;i++) {
    var match_pattern = array_titles[i].toLowerCase()
    if (page_url.indexOf(match_pattern) > -1) {
      match_no++;
    } 
   
  }
  return match_no;
}



function httpGet(theUrl)
{
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            return xmlhttp.responseText;
        }
    }
    xmlhttp.open("GET", theUrl, false );
    xmlhttp.send();    
}




function makeHttpObject() {
  try {return new XMLHttpRequest();}
  catch (error) {}
  try {return new ActiveXObject("Msxml2.XMLHTTP");}
  catch (error) {}
  try {return new ActiveXObject("Microsoft.XMLHTTP");}
  catch (error) {}

  throw new Error("Could not create HTTP request object.");
}
