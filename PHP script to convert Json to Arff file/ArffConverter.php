<?php
/**
 * Created by PhpStorm.
 * User: rushikesh
 * Date: 7/28/15
 * Time: 8:23 PM
 */

class ArffConverter {
    private $header = "@relation Phishing.Detection\n\n@attribute hostname string
 @attribute ipaddress {True,False}
 @attribute portNumber {True,False}
 @attribute protocol {http,https,other}
 @attribute domain_in_hostname {True,False}
 @attribute obfuscation_characters {True,False}
 @attribute subdomain_path {True,False}
 @attribute max_length integer
 @attribute phising {True,False}

 @data \n";

    public function getHeader()
    {
        return $this->header;
    }
}