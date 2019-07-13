<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class EstateImg extends Model
{
    
    public $timestamps = false;
    protected $table = 'estate_imgs';
    protected $fillable = ['img_loc','estate_id'];

    public function estate(){
        return $this->belongsTo('App\Estate');
    }
}
