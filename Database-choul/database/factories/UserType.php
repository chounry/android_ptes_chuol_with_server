<?php

/* @var $factory \Illuminate\Database\Eloquent\Factory */

use App\UserTypeModel;
use Faker\Generator as Faker;

$factory->define(UserTypeModel::class, function (Faker $faker) {
    return [
        'name' => 'Admin'
    ];
});
