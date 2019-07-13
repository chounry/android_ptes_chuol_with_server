<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSavedPostTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('saved_post', function (Blueprint $table) {
            $table->unsignedInteger('user_id');
            $table->unsignedInteger('estate_id');
            $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
            $table->foreign('estate_id')->references('id')->on('estates')->onDelete('cascade');

        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('saved_post', function (Blueprint $table) {
            $table->dropForeign(['user_id']);
            $table->dropForeign(['estate_id']);
        });
        Schema::dropIfExists('saved_post');
    }
}
