<?php
/**
 * Created by PhpStorm.
 * User: rushikesh
 * Date: 7/26/15
 * Time: 3:34 PM
 */

class PhishingParameters {

    private $domains = array(
        "com",
        "net",
        "org",
        "uk",
        "in",
        "us"
    );


    /*private $parameters = array(
    "hostname"              => '',
    "path"                  => '',
    "portNumber"            => false,
    "protocol"              => 'https',
    "domain_in_hostname"    => false,
    "obfuscation_characters"=> false,
    "subdomain_path"        => false,
    "ipaddress"             => false,
    );*/


    public function  GetParameters($domainUrl) {
        //echo $domainUrl;
        $parameters = array();
        /*********code to get hostname and Path*******************************/
        //echo "<h2>Host Name and Path </h2>";
        preg_match('@^(?:(http|https)://)?(?P<hostanme>[^/]+)/(?P<path>.*)@i', $domainUrl, $hostname);
        $parameters['hostname'] = $hostname['hostanme'];
        //echo "<pre>";
        //print_r($hostname);
        /****************************************************************************************/



        /*********code to find similarity between title and URL*******************************/
        /****************************************************************************************/



        /***********code to detect Ip address***************************/
        //echo "<h2>IP ADDRESS </h2>";
        preg_match('/^((http|https):\/\/(?P<IpAddress>\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}))/',$domainUrl,$ip_address);
        //echo "<pre>";
        if(isset($ip_address['IpAddress'])) {
            $parameters['ipaddress'] = 'True';
        }
        else {
            $parameters['ipaddress'] = 'False';
        }
        //print_r($ip_address);
        /****************************************************************************************/


        /***********code to detect port number in url***************************/
        //echo "<h2>Port Number </h2>";
        preg_match('/^((http|https):\/\/www.\w*.\w*):(?P<digit>\d+)/',$domainUrl,$port_number);
        if(isset($port_number['digit'])) {
            $parameters['portNumber'] = 'True';
        }
        else {
            $parameters['portNumber'] = 'False';
        }
        //echo "<pre>";
        //print_r($port_number);
        /****************************************************************************************/


        /*************cdoe to detect protocol**************************************************/
        //echo "<h2>Protocol </h2>";
        preg_match('/^(?P<protocol>(http|https)):\/\//',$domainUrl,$protocol);
        //echo "<pre>";
        //print_r($protocol);
        if(isset($protocol['protocol'])) {
            $parameters['protocol'] = $protocol['protocol'];
        }
        else {
            $parameters['protocol'] = "other";
        }
        /****************************************************************************************/


        /***************code to detect whether domains embedded in hostname**********************/
        //echo "<h2>Domains embedded in hostname</h2>";
        $domains_embedded_hostname = explode(".",$hostname['hostanme']);
        //echo "<pre>";
        //print_r($domains_embedded_hostname);
        if(sizeof($domains_embedded_hostname)>4) {
            $parameters['domain_in_hostname'] = 'True';
        }
        else {
            $parameters['domain_in_hostname'] = 'False';
        }
        /****************************************************************************************/


        /**************code to detect presence of obfuscation with unusual characters***********/
        //echo "<h2>presence of obfuscation with unusual characters</h2>";
        //preg_match('/\%20/',$hostname['path'],$obfuscation_char);
        //echo "<pre>";
        //print_r($obfuscation_char);
        if(preg_match('/\%20/',$hostname['path'],$obfuscation_char)) {
            $parameters['obfuscation_characters'] = 'True';
        }
        else {
            $parameters['obfuscation_characters'] = 'False';
        }
        /****************************************************************************************/


        /*********code to detect subdomain in pathname******************************************/

        //echo "<h2>Multiple Domains</h2>";
        preg_match('/(?:[-A-Za-z0-9]+\.)+[A-Za-z]{2,6}/',$hostname['path'],$multopledomains);
        //echo "<pre>";
        //print_r($multopledomains);

        // update this logic its not proper
        $pathname = explode('/',$hostname['path']);
        $max_char = 0;
        $parameters['max_length'] = 0;
        $parameters['subdomain_path'] = 'False';
        for ($i=0 ;$i < (sizeof($pathname)-1) ;$i++) {
            if(preg_match('/(?:[-A-Za-z0-9]+\.)+[A-Za-z]{2,6}/',$pathname[$i],$multipledomains)) {
                $parameters['subdomain_path'] = 'True';
            }
            if ($max_char < strlen($pathname[$i])) {
                $parameters['max_length'] = strlen($pathname[$i]);
                $max_char = strlen($pathname[$i]);
            }
        }

        if (substr_count($pathname[sizeof($pathname) -1], '.') > 1) {
            $parameters['subdomain_path'] = 'True';
        }
        /****************************************************************************************/

        //echo "<br><br>HEHREHREHRHERH<pre>";
        //$url_properties = $parameters;
        //print_r($parameters);
        //echo "HERHEHREHREHEHREHR<br><br>";

        $parameters['phishing'] = 'True';
        $parameters['path'] = $hostname['path'];

        return $parameters;
    }
}