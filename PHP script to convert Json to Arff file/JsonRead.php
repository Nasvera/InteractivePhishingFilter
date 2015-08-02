<?php
/**
 * Created by PhpStorm.
 * User: rushikesh
 * Date: 7/12/15
 * Time: 4:03 PM
 */

include "PhishingParameters.php";

class JsonReadLib{

    public function jsonRead($jsonObject) {
        $jsonarray = json_decode($jsonObject);
        /*echo "<pre>";
        print_r($jsonarray);*/


        $phishing_para = new PhishingParameters();

        //echo "<pre>";
        //print_r($url_parameters);


        //$json_array_updated = array();
        $i = 0;
        foreach($jsonarray as $key) {
            $url_parameters[$i] = $phishing_para->GetParameters($key->url);
            //$json_array_updated[$i]['url'] = $key->url;
            //print_r($key);
            //exit;
            $i++;
        }

        //echo "Total Values :: $i <br>";
        return $url_parameters;

        /*print_r($jsonarray[0]);
        $json_array_updated[0]['url'] = $jsonarray[0]->url;
        $json_array_updated[0]['details'] = $jsonarray[0]->details;*/
        //$json_array_updated = json_encode($json_array_updated);
        //return $json_array_updated;
    }


    public function jsontoString($jsobObject) {
        $jsonToString = '';
        $jsonArray = json_decode($jsobObject);

        foreach($jsonArray as $key) {
            /*$hostname = $key['hostname'];
            $ipaddress = $key['ipaddress'];
            $portNumber = $key['portNumber'];
            $protocol = $key['protocol'];
            $domain_in_hostname = $key['domain_in_hostname'];
            $obfuscation_characters = $key['obfuscation_characters'];
            $subdomain_path = $key['subdomain_path'];
            $phishing = $key['phishing'];*/

            $jsonToString_sinlge_instance = '';
            $hostname = $key->hostname;
            $ipaddress = $key->ipaddress;
            $portNumber = $key->portNumber;
            $protocol = $key->protocol;
            $domain_in_hostname = $key->domain_in_hostname;
            $obfuscation_characters = $key->obfuscation_characters;
            $subdomain_path = $key->subdomain_path;
            $phishing = $key->phishing;
            $max_length = $key->max_length;

            $jsonToString_sinlge_instance .= '\'' . $hostname . '\',';
            $jsonToString_sinlge_instance .= $ipaddress . ',' . $portNumber . ',' . $protocol . ',' . $domain_in_hostname . ',' . $obfuscation_characters . ',' . $subdomain_path . ',' . $max_length . ',' . $phishing . "\n";
            $jsonToString .= $jsonToString_sinlge_instance;

        }

        return $jsonToString;
    }
}

