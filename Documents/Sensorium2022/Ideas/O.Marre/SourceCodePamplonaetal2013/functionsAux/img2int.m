function [x1,y1] = img2int(u, v,uData, vData,pixel_size)
%transform coordinates in the image plan to intrinsic
%uData and vData are the image minimum  and maximum pixel coordinates. U 
%corresponds to rows and v to columns. By default uData(1) and vData(1) 
%should be 1 and uData(2) and vData(2) should be the image_size 
% pixel_size:size (in mm) of a pixel (if a camera, usual sensor array size is 45mmx34mm) (pixel size = (number of pixels per row)/(horizontal sensor size)) (in our case the images are square, and the pixels as well)

[x,y] = img2hom(u,v,uData, vData, pixel_size);
[x1,y1] = hom2int(x,y);
