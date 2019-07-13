<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Estate extends Model
{
    public $timestamps = false;
    protected $table = 'estates';
    protected $fillable = ['title','price','description','phone','phone_option','address','accepted','date','publish','lat','lng','currency','duration','city_id','user_id'];

    public function city(){
        return $this->belongsTo('App\City');
    }

    public function estate_imgs(){
        return $this->hasMany('App\EstateImg');
    }

    public function house(){
        return $this->hasOne('App\House');
    }

    public function room(){
        return $this->hasOne('App\Room');
    }
}
