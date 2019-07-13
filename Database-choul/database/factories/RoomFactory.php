<?php

/* @var $factory \Illuminate\Database\Eloquent\Factory */

use App\RoomModel;
use Faker\Generator as Faker;

$factory->define(RoomModel::class, function (Faker $faker) {

    return [
        'size' => $faker->randomDigit,
        'free_wifi' => 0,
        'parking_space_avalible' => 1,
        'AC' => 1,
        'estate_id' => 1,

    ];
});
