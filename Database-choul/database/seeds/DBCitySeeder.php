<?php

use Illuminate\Database\Seeder;
use App\CitiesModel;

class DBCitySeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        CitiesModel::create(['name' => 'Phnom Penh']);
        CitiesModel::create(['name' => 'Kompot']);
        CitiesModel::create(['name' => 'Kompong Chhnang']);
        CitiesModel::create(['name' => 'Kandal']);
        CitiesModel::create(['name' => 'KPS']);
    }
}
