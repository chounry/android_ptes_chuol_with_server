<?php

/* @var $factory \Illuminate\Database\Eloquent\Factory */

use App\EstateModel;
use Faker\Generator as Faker;

$factory->define(EstateModel::class, function (Faker $faker) {
    return [
        'title' => $faker->name,
            'price' => $faker->randomDigit ,
            'description' => $faker->text ,
            'address' => $faker->address,
            'date' => $faker->date,
            'publish' => 1,
            'city_id' => 1,
            'user_id' => 2
    ];
});
