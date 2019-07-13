<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class City extends Model
{
    public $timestamps = false;
    protected $table = 'cities';
    protected $fillable = ['name'];


    public function estates(){
        return $this->hasMany('App\Estate');
    }
}
