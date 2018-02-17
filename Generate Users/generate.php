<?php
require_once './vendor/autoload.php';
$faker = Faker\Factory::create();

$admin = '{
    "username": "admin",
    "email": "ghettinger@example.org",
    "password": "Passw0rd",
    "fullname": "Administrator",
    "street": "Aba Khoushy Ave",
    "city": "Haifa",
    "zip": "3498838",
    "telephone": "0578102611",
    "nickname": "admin",
    "bio": "Queen will hear.",
    "street_number": 199,
    "is_admin": true
}';
$admin = json_decode($admin);

$phonePrefixes = [
    '050',
    '051',
    '052',
    '053',
    '054',
    '055',
    '056',
    '057',
    '058',
    '059',
    '02',
    '03',
    '04',
    '08',
    '09',
];

$users = [$admin];
for ($i=0; $i < 20; $i++) {
    $email = $faker->safeEmail;
    $users[] = [
        'username' => substr($faker->username, 0, 10),
        'email' => $email,
        'password' => substr($faker->password, 0, 8),
        'fullname' => $faker->name,
        'street' => $faker->streetName,
        'city' => $faker->city,
        'zip' => (string)$faker->randomNumber(7),
        'telephone' => $faker->randomElement($phonePrefixes) . $faker->randomNumber(7),
        'nickname' => substr(explode('@', $email)[0], 0, 20),
        'bio' => $faker->realText($faker->numberBetween(10, 50)),
        'street_number' => $faker->numberBetween(1, 240),
        'is_admin' => false,
    ];

}
file_put_contents('./users.json', json_encode($users, JSON_PRETTY_PRINT));
echo "Generated 20 users\n";
