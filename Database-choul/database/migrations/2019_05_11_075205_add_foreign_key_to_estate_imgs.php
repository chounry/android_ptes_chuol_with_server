<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class AddForeignKeyToEstateImgs extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('estate_imgs', function (Blueprint $table) {
            $table->foreign('estate_id')->references('id')->on("estates");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('estate_imgs', function (Blueprint $table) {
            $table->dropForeign(['estate_id']);
        });
    }
}
