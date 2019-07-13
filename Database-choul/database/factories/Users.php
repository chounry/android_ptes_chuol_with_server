<?php

/* @var $factory \Illuminate\Database\Eloquent\Factory */

use App\UserModel;
use Faker\Generator as Faker;

$factory->define(App\UserModel::class, function (Faker $faker) {
    return [
        'fname' => $faker->name,
        'lname' => $faker->name,
        'phone' => $faker->phoneNumber,
        'email' => $faker->unique()->safeEmail,
        'img_loc' => "string",
        'user_type_id' => 1

    ];
});
