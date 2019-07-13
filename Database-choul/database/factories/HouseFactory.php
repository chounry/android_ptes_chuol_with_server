<?php

/* @var $factory \Illuminate\Database\Eloquent\Factory */

use App\HouseModel;
use Faker\Generator as Faker;

$factory->define(HouseModel::class, function (Faker $faker) {
    return [
        'bedroom' => $faker->randomDigit,
        'bathroom' => $faker->randomDigit ,
        'floor' => $faker->randomDigit ,
        'house_size' => $faker->randomDigit,
        'yard_size' => $faker->randomDigit,
        'for_sale_status' => $faker->randomDigit,
        'estate_id' => 1,
        'house_type_id' => 1
    ];
});
