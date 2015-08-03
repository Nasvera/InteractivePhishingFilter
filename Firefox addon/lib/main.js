require("sdk/tabs").on("ready", logURL);

var domain_list = [".com",".net",".org",".uk",".in",".us"];
var search_engine = ["www.google.com","r.search.yahoo.com","www.bing.com","search.yahoo.com"];
var buttons = require('sdk/ui/button/action');

const {Cc,Ci} = require("chrome")
const prefs = require("simple-prefs");

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
    
  /*********code to find similarity between title and URL*******************************/
  var array_titles = page_title.split(" ");
  var similarity_per = (find_similarity(array_titles) / array_titles.length) * 100;
  if (similarity_per==0) {
    parameter_1 = true;
  }
  /*************************************************************************************/
  
  /***********code to detect Ip address and port number in url***************************/
  var reg_ip = /\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b/;
  var reg_domain = /\bwww\.\w+\.\w+\b/;
  var host_ip = reg_ip.exec(host);
  if (host_ip==null) {
    //console.log("No IP address in host");
  }
  else {
    if (port==null) {
      //console.log("IP address in host");  
    }
    else {
      //console.log("IP address with port in host.");
      parameter_2 = true;
    }
  }
  /***************************************************************************************/
  
  /*************cdoe to detect protocol**************************************************/
  var secure_http = false;
  if (!(url.protocol=="https:")) {
    secure_http = true;
    parameter_3 = true;
  }
  /**************************************************************************************/

  /***************code to detect whether domains embedded in hostname**********************/
  num_of_domains_in_host = 0;
  for (var i = domain_list.length - 1; i >= 0; i--) {
    var new_reg = "\\" + domain_list[i];
      var reg_domains = new RegExp(new_reg,'g');
      temp_count = host.match(reg_domains);
      if (!(temp_count==null)) {
        num_of_domains_in_host = temp_count.length + num_of_domains_in_host;  
      }
  }
  if (num_of_domains_in_host>1) {
    parameter_4 = true;
  }
  /***************************************************************************************/

  /**************code to detect presence of obfuscation with unusual characters***********/
  if (pathname.indexOf("%20") > -1 ) {
    parameter_5 = true;
  }
  /****************************************************************************************/
    
  /*********code to detect subdomain in pathname******************************************/
  var contain_subdomain = false;
  for (var i = domain_list.length - 1; i >= 0; i--) {
    if (pathname.indexOf("." + domain_list[i]) > -1)
      contain_subdomain = true;
  }
  if (pathname.indexOf("www.") > -1) {
    contain_subdomain = true;
  }
  if (contain_subdomain) {
    parameter_6 = true;
  }
 
  phishing = false;
  search_engine_bool = false;
  if (parameter_3) {
    if (parameter_2) {
      if ((parameter_6) || (parameter_5) || (parameter_1)) {
        search_engine_bool = is_search_engine(host);
        if (!(search_engine_bool)) {
          phishing = true;
        } 
      }
    }
    else {
      if ((parameter_6) || (parameter_5) || (parameter_1) || (parameter_4)) {
        search_engine_bool = is_search_engine(host);
        if (!(search_engine_bool)) {
          phishing = true;
        }
      }
    }
  }

  if (phishing) {
    var prompts = Cc["@mozilla.org/embedcomp/prompt-service;1"]
                        .getService(Ci.nsIPromptService);

    var check = {value: false};                  // default the checkbox to false

    var flags = prompts.BUTTON_POS_0 * prompts.BUTTON_TITLE_SAVE +
                prompts.BUTTON_POS_1 * prompts.BUTTON_TITLE_CANCEL;
    // This value of flags will create 3 buttons. The first will be "Save", the
    // second will be the value of aButtonTitle1, and the third will be "Cancel"

    var button1 = prompts.confirmEx(null, "Phishing Attack Ahead!!!", "This Website might contain some unwanted code which may try to theft you confideintial data whihout of your knowledge. If you feel that this website is safe and secure to use then press \"Continue\" otherwise press \"Cancel\".",
                                   flags, "", "", "", null, check);
    //console.log("Pressed Button is :" + button1);
    button.state(button, differentState);
  }
  else{
    button.state(button, defaultState);
  }
  

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