#!/bin/sh

convert background-banner.jpg -scale 100x150 ../../war/images/background-banner.jpg

convert jyf.jpg -scale 60x60 ../../war/images/jyf.jpg

cd $war/images
jpegoptim --strip-all *.jpg
