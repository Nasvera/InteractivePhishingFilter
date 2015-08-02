<?php
/**
 * Created by PhpStorm.
 * User: rushikesh
 * Date: 8/1/15
 * Time: 3:17 PM
 */

require_once "PhishingParameters.php";

$getObj = new PhishingParameters();
$parameters = $getObj->GetParameters("http://herpnation.com/Online-Lloyds-vayeC0yhhECDeN5Q42MpHy3x2/faillure.php");
echo "<pre>";
print_r($parameters);
