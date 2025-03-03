<?php
function fetchDataFromApi($url, $bearerToken) {
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        "Authorization: Bearer $bearerToken",
        "Content-Type: application/json"
    ]);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode !== 200) {
        return null;
    }

    return json_decode($response, true);
}

function fetchDataFromApiWithBody($url, $body ,$bearerToken) {
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, [
        "Authorization: Bearer $bearerToken",
        "Content-Type: application/json"
    ]);
    curl_setopt($ch,CURLOPT_POSTFIELDS, $body);

    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if($httpCode == 302){
        return json_decode($response, true);
    }elseif ($httpCode == 400) {
        return "Bad Request";
    }elseif ($httpCode == 401) {
        return "Unauthorized";
    } elseif ($httpCode == 404) {
        return "Not Found";
    } else{
        return null;
    }

}