<?php

$txtonly = function($name) {
    return preg_match("/\.txt$/", $name);
};

$reviews = [];
$files = array_merge(array_map(function($name) {
    return "pos/{$name}";
}, array_filter(scandir('./pos'), $txtonly)), array_map(function($name) {
    return "neg/{$name}";
}, array_filter(scandir('./neg'), $txtonly)));

$reviews = array_map(function($file) {
    $t = file_get_contents($file);
    $sentences = array_map(function($s) {
        return str_replace(" , ", ", ", rtrim($s));
    }, explode('.', $t));
    $num = mt_rand(5, 8);

    return trim(implode('.', array_slice($sentences, 0, $num)));
}, $files);

file_put_contents("./reviews.json", json_encode($reviews));