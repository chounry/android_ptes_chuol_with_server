<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateHousesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create("houses", function(Blueprint $table){
            $table->increments('id');
            $table->tinyInteger('bedroom')->unsigned();
            $table->tinyInteger('bathroom')->unsigned();
            $table->integer('floor');
            $table->string('house_size');
            $table->string('yard_size');
            $table->string('for_sale_status');
            $table->unsignedInteger('estate_id');
            $table->unsignedInteger('house_type_id');
        });  
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("houses");
    }
}
