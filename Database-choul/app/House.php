<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class House extends Model
{
    public $timestamps = false;
    protected $table = 'houses';
    protected $fillable = ['bedroom','bathroom','floor','house_size','yard_size','for_sale_status','house_type_id','estate_id'];


    public function house_type(){
        return $this->belongsTo('App\HouseType');
    }
}
