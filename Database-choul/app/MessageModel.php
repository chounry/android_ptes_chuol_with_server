<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class MessageModel extends Model
{
    public $timestamps = false;
    protected $table = 'messages';
    protected $fillable = ['estate_id','content','seen','from_self_user_id','to_user_user_id','date'];
}
