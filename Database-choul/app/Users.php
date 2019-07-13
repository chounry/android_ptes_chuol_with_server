<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Users extends Model
{
    public $timestamps = false;
    protected $table = 'users';
    protected $fillable = ['fname','lname','email','phone','img_loc','user_type_id'];

    public function saved_posts(){
        return $this->belongsToMany('App\Estate','saved_post','user_id','estate_id');
    }

}
