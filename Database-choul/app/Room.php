<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Room extends Model
{
    public $timestamps = false;
    protected $table = 'rooms';
    protected $fillable = ['size','free_wifi','parking_space_avalible','AC','estate_id'];
}
