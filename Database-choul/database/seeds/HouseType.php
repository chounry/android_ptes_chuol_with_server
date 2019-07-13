<?php

use App\HouseTypeModel;
use Illuminate\Database\Seeder;

class HouseType extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        HouseTypeModel::create(['name' => 'Villa']);
        HouseTypeModel::create(['name' => 'Flat']);
        HouseTypeModel::create(['name' => 'Twin House']);
    
    }
}
