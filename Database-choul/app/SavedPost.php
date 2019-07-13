<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class SavedPost extends Model
{
    public $timestamps = false;
    protected $table = 'saved_post';
    protected $fillable = ['estate_id','user_id'];
}
