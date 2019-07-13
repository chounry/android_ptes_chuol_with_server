<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserType extends Model
{
    public $timestamps = false;
    protected $table = 'user_types';
    protected $fillable = ['name'];
}
