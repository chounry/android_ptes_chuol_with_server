<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class HouseType extends Model
{
    
    public $timestamps = false;
    protected $table = 'house_types';
    protected $fillable = ['name'];

    public function houses(){
        return $this->hasManay('App\HouseModel');
    }
}
