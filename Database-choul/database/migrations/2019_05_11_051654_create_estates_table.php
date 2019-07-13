<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateEstatesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create("estates", function(Blueprint $table){
            $table->increments('id');
            $table->string('title');
            $table->float('price')->nullable();
            $table->string('description')->nullable();
            $table->string('phone');
            $table->string('address');
            $table->date('date');
            $table->double('lat');
            $table->double('lng');
            $table->boolean('accepted');
            $table->boolean('publish');
            $table->string('currency');
            $table->string('duration');
            $table->integer('phone_option')->nullable();
            $table->unsignedInteger('city_id');
            $table->unsignedInteger('user_id');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("estates");
    }
}
