<?php
/**
 * Created by PhpStorm.
 * User: rushikesh
 * Date: 7/12/15
 * Time: 4:07 PM
 */

require_once "JsonRead.php";
require_once "ArffConverter.php";

try {


    /*$domains = array(
                    "com",
                    "net",
                    "org",
                    "uk",
                    "in",
                    "us"
    );


    $parameters = array(
        "hostname"              => '',
        "path"                  => '',
        "portNumber"            => false,
        "prototocl"             => 'https',
        "domain_in_hostname"    => false,
        "obfuscation_characters"=> false,
        "subdomain_path"        => false,
        "ipaddress"             => false,
    );*/
    /*echo "<br><br><pre>";
    print_r($domains);*/
    $url_without_ip = "https://www.php.net:6060/imag\\%20esd\\%20asf/www.google.com/index.html";
    $url_with_ip = "http://192.168.158.57:8980/index.jsp";
    $url_with_sudomain = "https://www.php.net:6060/www.google.com/index.html";
    /*preg_match('@^(?:http://)?([^/]+)@i',
        $url_with_sudomain, $matches);
    echo "<pre>";
    print_r($matches);
    $host = $matches[1];
    echo "HOST::  $host";*/


    /*********code to get hostname and Path*******************************/
    /*echo "<h2>Host Name and Path </h2>";
    preg_match('@^(?:(http|https)://)?(?P<hostanme>[^/]+)/(?P<path>.*)@i', $url_without_ip, $hostname);
    echo "<pre>";
    print_r($hostname);*/
    /*********code to find similarity between title and URL*******************************/

    /***********code to detect Ip address***************************/
    /*echo "<h2>IP ADDRESS </h2>";
    preg_match('/^((http|https):\/\/(?P<IpAddress>\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}))/',$url_with_ip,$ip_address);
    echo "<pre>";
    print_r($ip_address);*/

    /***********code to detect port number in url***************************/
    /*echo "<h2>Port Number </h2>";
    preg_match('/^((http|https):\/\/www.\w*.\w*):(?P<digit>\d+)/',$url_without_ip,$port_number);
    echo "<pre>";
    print_r($port_number);*/

    /*************cdoe to detect protocol**************************************************/
    /*echo "<h2>Protocol </h2>";
    preg_match('/^(?P<protocol>(http|https)):\/\//',$url_without_ip,$protocol);
    echo "<pre>";
    print_r($protocol);*/


    /***************code to detect whether domains embedded in hostname**********************/
    /*echo "<h2>Domains embedded in hostname</h2>";
    $domains_embedded_hostname = explode(".",$hostname['hostanme']);
    echo "<pre>";
    print_r($domains_embedded_hostname);*/


    /**************code to detect presence of obfuscation with unusual characters***********/
    /*echo "<h2>presence of obfuscation with unusual characters</h2>";
    preg_match('/\%20/',$hostname['path'],$obfuscation_char);
    echo "<pre>";
    print_r($obfuscation_char);*/


    /*********code to detect subdomain in pathname******************************************/

    /*echo "<h2>Multiple Domains</h2>";
    preg_match('/(?:[-A-Za-z0-9]+\.)+[A-Za-z]{2,6}/',$hostname['path'],$multopledomains);
    echo "<pre>";
    print_r($multopledomains);*/


    /*$myfile = fopen("Data/verified_online.json", "r");
    $jsobObject = fread($myfile, filesize("Data/verified_online.json"));
    fclose($myfile);
    $jsonlib = new JsonReadLib;
    $updated_array = $jsonlib->jsonRead($jsobObject);
    $new_file = fopen("Data/verified_online_udpated.json","w");
    fwrite($new_file, $updated_array);
    fclose($new_file);*/


    //echo "Rushikesh";
    $file = fopen('Data/verified_online_udpated.json','r');
    $file_content = fread($file,filesize('Data/verified_online_udpated.json'));
    fclose($file);
    $json_Read = new JsonReadLib();
    $parameters = $json_Read->jsonRead($file_content);
    //echo "<pre>";
    //print_r($parameters);


    $new_file = fopen("Data/phishing_parameters_for_weka.json.txt","w");
    $parameters = json_encode($parameters);
    /*echo "<pre>";
    var_dump($parameters);*/
    /*fwrite($new_file, $parameters);
    fclose($new_file);*/


    $arff_String = $json_Read->jsontoString($parameters);
    $new_file = fopen("Data/myfile.arff.txt","w");
    fwrite($new_file, $arff_String);
    fclose($new_file);

    $arrf = new ArffConverter();
    $arff_String = $arrf->getHeader();
    $arff_String .= $json_Read->jsontoString($parameters);
    $new_file = fopen("Data/phishing_wihoutInteger.arff","w");
    fwrite($new_file, $arff_String);
    fclose($new_file);

    echo "Arff file has been generated successfully.";
}

catch (Exception $e) {
    echo $e->getMessage();
}
